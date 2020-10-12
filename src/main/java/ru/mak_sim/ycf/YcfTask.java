/*
 *     Copyright 2020 Maxim Syomochkin <maksim77ster@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.mak_sim.ycf;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import yandex.cloud.api.serverless.functions.v1.FunctionServiceGrpc;
import yandex.cloud.sdk.ServiceFactory;
import yandex.cloud.sdk.auth.Auth;

abstract class YcfTask extends DefaultTask {
    public static final String TASK_GROUP = "Yandex Cloud Functions";
    YcfPluginExtension ycfExtension = this.getProject().getExtensions().getByType(YcfPluginExtension.class);
    Logger logger = this.getProject().getLogger();
    FunctionServiceGrpc.FunctionServiceBlockingStub funcService;
    ServiceFactory factory;

    public YcfTask() {
        this.setGroup(TASK_GROUP);
        if (ycfExtension.token == null) {
            logger.error("Missing Yandex Cloud oauth token!");
            throw new GradleException("Your must specify oauth token!");
        }
        factory = ServiceFactory.builder()
                .credentialProvider(
                        Auth.oauthTokenBuilder().oauth(ycfExtension.token)
                ).build();

        funcService = factory.create(FunctionServiceGrpc.FunctionServiceBlockingStub.class,
                FunctionServiceGrpc::newBlockingStub);
    }
}
