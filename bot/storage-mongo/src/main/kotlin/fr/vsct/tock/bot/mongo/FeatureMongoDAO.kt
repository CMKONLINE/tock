/*
 * Copyright (C) 2017 VSCT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.vsct.tock.bot.mongo

import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP
import com.mongodb.client.model.changestream.OperationType.DELETE
import com.mongodb.client.model.changestream.OperationType.INSERT
import com.mongodb.client.model.changestream.OperationType.INVALIDATE
import com.mongodb.client.model.changestream.OperationType.REPLACE
import com.mongodb.client.model.changestream.OperationType.UPDATE
import fr.vsct.tock.bot.engine.feature.FeatureDAO
import fr.vsct.tock.bot.engine.feature.FeatureState
import fr.vsct.tock.bot.mongo.Feature_.Companion.BotId
import fr.vsct.tock.bot.mongo.Feature_.Companion.Namespace
import fr.vsct.tock.bot.mongo.Feature_.Companion._id
import fr.vsct.tock.bot.mongo.MongoBotConfiguration.asyncDatabase
import fr.vsct.tock.bot.mongo.MongoBotConfiguration.database
import fr.vsct.tock.shared.error
import fr.vsct.tock.shared.watchSafely
import mu.KotlinLogging
import org.bson.BsonString
import org.litote.kmongo.Data
import org.litote.kmongo.async.getCollection
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.eq
import org.litote.kmongo.find
import org.litote.kmongo.getCollection
import org.litote.kmongo.save
import java.util.concurrent.ConcurrentHashMap

@Data(internal = true)
internal data class Feature(
    val _id: String,
    val key: String,
    val enabled: Boolean,
    val botId: String,
    val namespace: String
)

/**
 *
 */
internal object FeatureMongoDAO : FeatureDAO {

    private val logger = KotlinLogging.logger {}
    private val col = database.getCollection<Feature>()
    private val asyncCol = asyncDatabase.getCollection<Feature>()
    private val features = ConcurrentHashMap<String, Boolean>()

    init {
        try {
            asyncCol.find().forEach({
                features[it._id] = it.enabled
            }) { _, t -> if (t != null) logger.error(t) }
            asyncCol.watchSafely({ it.fullDocument(UPDATE_LOOKUP) }) {
                println(it)
                when (it.operationType) {
                    DELETE, INVALIDATE -> (it.documentKey[_id.name] as? BsonString)?.value?.also {
                        features.remove(
                            it
                        )
                    }
                    REPLACE, UPDATE, INSERT -> it.fullDocument?.also { features[it._id] = it.enabled }
                }
            }
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    private fun calculateId(botId: String, namespace: String, category: String, name: String) =
        "$botId,$namespace,$category,$name"


    override fun isEnabled(
        botId: String,
        namespace: String,
        category: String,
        name: String,
        default: Boolean
    ): Boolean =
        features[calculateId(botId, namespace, category, name)] ?: default

    override fun enable(botId: String, namespace: String, category: String, name: String) {
        val id = calculateId(botId, namespace, category, name)
        features[id] = true

        asyncCol.replaceOne(
            _id eq id,
            Feature(id, "$category,$name", true, botId, namespace),
            ReplaceOptions().upsert(true)
        ) { _, t ->
            if(t != null) {
                logger.error(t)
            }
        }
    }

    override fun disable(botId: String, namespace: String, category: String, name: String) {
        val id = calculateId(botId, namespace, category, name)
        features[id] = false
        asyncCol.replaceOne(
            _id eq id,
            Feature(id, "$category,$name", false, botId, namespace),
            ReplaceOptions().upsert(true)
        ) { _, t ->
            if(t != null) {
                logger.error(t)
            }
        }
    }

    override fun getFeatures(botId: String, namespace: String): List<FeatureState> =
        col.find(BotId eq botId, Namespace eq namespace)
            .mapNotNull {
                try {
                    val index = it.key.lastIndexOf(',')
                    val category = if (index == -1) "" else it.key.substring(0, index)
                    val name = if (index == -1) it.key else it.key.substring(index + 1, it.key.length)
                    FeatureState(category, name, it.enabled)
                } catch (e: Exception) {
                    logger.error(e)
                    null
                }
            }

    override fun addFeature(botId: String, namespace: String, enabled: Boolean, category: String, name: String) {
        col.save(Feature(calculateId(botId, namespace, category, name), "$category,$name", enabled, botId, namespace))
    }

    override fun deleteFeature(botId: String, namespace: String, category: String, name: String) {
        val id = calculateId(botId, namespace, category, name)
        features.remove(id)
        col.deleteOneById(id)
    }
}