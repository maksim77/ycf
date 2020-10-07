package ru.mak_sim.ycf;

import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import yandex.cloud.api.operation.OperationOuterClass;
import yandex.cloud.api.operation.OperationServiceGrpc;
import yandex.cloud.api.operation.OperationServiceOuterClass;
import yandex.cloud.api.serverless.functions.v1.FunctionOuterClass;
import yandex.cloud.api.serverless.functions.v1.FunctionServiceOuterClass;

import java.io.IOException;
import java.nio.file.Files;

public class YcfTaskCreateVersion extends YcfTask {
    public YcfTaskCreateVersion() {
        this.setDescription("Create new yandex cloud function version");
        this.dependsOn(this.getProject().getTasks().getByName("distZip"));
    }

    @TaskAction
    public void run() {
        if (ycfExtension.function_id == null) {
            throw new GradleException("You must specify function_id. A list of all functions is available in the ycfList task");
        }
        if (ycfExtension.entrypoint == null) {
            throw new GradleException("You must specify entrypoint");
        }

        OperationServiceGrpc.OperationServiceBlockingStub operationsService = factory.create(OperationServiceGrpc.OperationServiceBlockingStub.class,
                OperationServiceGrpc::newBlockingStub);

        FunctionServiceOuterClass.CreateFunctionVersionRequest.Builder createFuncVerReqBuilder = FunctionServiceOuterClass.CreateFunctionVersionRequest.newBuilder();
        createFuncVerReqBuilder
                .setFunctionId(ycfExtension.function_id)
                .setRuntime(ycfExtension.runtime)
                .setEntrypoint(ycfExtension.entrypoint)
                .setResources(FunctionOuterClass.Resources.newBuilder().setMemory(ycfExtension.memory).build())
                .build();

        if (ycfExtension.execution_timeout != null) {
            createFuncVerReqBuilder.setExecutionTimeout(Duration.newBuilder().setSeconds(ycfExtension.execution_timeout).build());
        }
        if (ycfExtension.description != null) {
            createFuncVerReqBuilder.setDescription(ycfExtension.description);
        }

        if (ycfExtension.environment != null) {
            createFuncVerReqBuilder.putAllEnvironment(ycfExtension.environment);
        }
        if (ycfExtension.tags != null) {
            createFuncVerReqBuilder.addAllTag(ycfExtension.tags);
        }

        try {
            byte[] bytes = Files.readAllBytes(this.getProject().getTasks().getByName("distZip").getOutputs().getFiles().getSingleFile().toPath());
            createFuncVerReqBuilder.setContent(ByteString.copyFrom(bytes));
        } catch (IOException e) {
            throw new GradleException("Cannot read file");
        }

        OperationOuterClass.Operation op = funcService.createVersion(createFuncVerReqBuilder.build());
        OperationServiceOuterClass.GetOperationRequest operationRequest = OperationServiceOuterClass.GetOperationRequest.newBuilder().setOperationId(op.getId()).build();
        logger.info("Updating functions");
        do {
        } while (!operationsService.get(operationRequest).getDone());
        logger.info("Function updated");
    }
}