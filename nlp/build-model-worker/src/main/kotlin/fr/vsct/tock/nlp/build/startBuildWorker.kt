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

package fr.vsct.tock.nlp.build

import com.github.salomonbrys.kodein.Kodein
import fr.vsct.tock.nlp.front.ioc.FrontIoc
import fr.vsct.tock.shared.vertx.vertx
import io.vertx.core.DeploymentOptions

fun main(args: Array<String>) {
    startBuildWorker()
}

fun startBuildWorker(vararg modules: Kodein.Module) {
    FrontIoc.setup(*modules)
    val buildModelWorkerVerticle = BuildModelWorkerVerticle()
    vertx.deployVerticle(buildModelWorkerVerticle, DeploymentOptions().setWorker(true))
    vertx.deployVerticle(CleanupModelWorkerVerticle(), DeploymentOptions().setWorker(true))
    vertx.deployVerticle(HealthCheckVerticle(buildModelWorkerVerticle))
}

