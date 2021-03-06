package fr.vsct.tock.nlp.front.shared.config

import fr.vsct.tock.nlp.core.PredefinedValue_Col
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Collection
import kotlin.collections.Map
import kotlin.reflect.KProperty1
import org.litote.kmongo.Id
import org.litote.kmongo.property.KCollectionPropertyPath
import org.litote.kmongo.property.KMapPropertyPath
import org.litote.kmongo.property.KPropertyPath

class EntityTypeDefinition_<T>(previous: KPropertyPath<T, *>?, property: KProperty1<*, EntityTypeDefinition?>) : KPropertyPath<T, EntityTypeDefinition?>(previous,property) {
    val name_: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::name)

    val description: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::description)

    val subEntities: EntityDefinition_Col<T>
        get() = EntityDefinition_Col(this,EntityTypeDefinition::subEntities)

    val predefinedValues: PredefinedValue_Col<T>
        get() = PredefinedValue_Col(this,EntityTypeDefinition::predefinedValues)

    val _id: KPropertyPath<T, Id<EntityTypeDefinition>?>
        get() = org.litote.kmongo.property.KPropertyPath<T, org.litote.kmongo.Id<fr.vsct.tock.nlp.front.shared.config.EntityTypeDefinition>?>(this,EntityTypeDefinition::_id)
    companion object {
        val Name: KProperty1<EntityTypeDefinition, String?>
            get() = EntityTypeDefinition::name
        val Description: KProperty1<EntityTypeDefinition, String?>
            get() = EntityTypeDefinition::description
        val SubEntities: EntityDefinition_Col<EntityTypeDefinition>
            get() = EntityDefinition_Col<EntityTypeDefinition>(null,EntityTypeDefinition::subEntities)
        val PredefinedValues: PredefinedValue_Col<EntityTypeDefinition>
            get() = PredefinedValue_Col<EntityTypeDefinition>(null,EntityTypeDefinition::predefinedValues)
        val _id: KProperty1<EntityTypeDefinition, Id<EntityTypeDefinition>?>
            get() = EntityTypeDefinition::_id}
}

class EntityTypeDefinition_Col<T>(previous: KPropertyPath<T, *>?, property: KProperty1<*, Collection<EntityTypeDefinition>?>) : KCollectionPropertyPath<T, EntityTypeDefinition?, EntityTypeDefinition_<T>>(previous,property) {
    val name_: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::name)

    val description: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::description)

    val subEntities: EntityDefinition_Col<T>
        get() = EntityDefinition_Col(this,EntityTypeDefinition::subEntities)

    val predefinedValues: PredefinedValue_Col<T>
        get() = PredefinedValue_Col(this,EntityTypeDefinition::predefinedValues)

    val _id: KPropertyPath<T, Id<EntityTypeDefinition>?>
        get() = org.litote.kmongo.property.KPropertyPath<T, org.litote.kmongo.Id<fr.vsct.tock.nlp.front.shared.config.EntityTypeDefinition>?>(this,EntityTypeDefinition::_id)

    @Suppress("UNCHECKED_CAST")
    override fun memberWithAdditionalPath(additionalPath: String): EntityTypeDefinition_<T> = EntityTypeDefinition_(this, customProperty(this, additionalPath))}

class EntityTypeDefinition_Map<T, K>(previous: KPropertyPath<T, *>?, property: KProperty1<*, Map<K, EntityTypeDefinition>?>) : KMapPropertyPath<T, K, EntityTypeDefinition?, EntityTypeDefinition_<T>>(previous,property) {
    val name_: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::name)

    val description: KPropertyPath<T, String?>
        get() = org.litote.kmongo.property.KPropertyPath<T, kotlin.String?>(this,EntityTypeDefinition::description)

    val subEntities: EntityDefinition_Col<T>
        get() = EntityDefinition_Col(this,EntityTypeDefinition::subEntities)

    val predefinedValues: PredefinedValue_Col<T>
        get() = PredefinedValue_Col(this,EntityTypeDefinition::predefinedValues)

    val _id: KPropertyPath<T, Id<EntityTypeDefinition>?>
        get() = org.litote.kmongo.property.KPropertyPath<T, org.litote.kmongo.Id<fr.vsct.tock.nlp.front.shared.config.EntityTypeDefinition>?>(this,EntityTypeDefinition::_id)

    @Suppress("UNCHECKED_CAST")
    override fun memberWithAdditionalPath(additionalPath: String): EntityTypeDefinition_<T> = EntityTypeDefinition_(this, customProperty(this, additionalPath))}
