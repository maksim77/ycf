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
