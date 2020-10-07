package ru.mak_sim.ycf;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import yandex.cloud.api.resourcemanager.v1.*;
import yandex.cloud.api.serverless.functions.v1.FunctionOuterClass.Function;
import yandex.cloud.api.serverless.functions.v1.FunctionServiceOuterClass;

import java.util.List;

public class YcfTaskList extends YcfTask {
    public YcfTaskList() {
        this.setDescription("List yandex cloud functions");
    }

    @TaskAction
    public void run() {
        CloudServiceGrpc.CloudServiceBlockingStub cloudService = factory.create(CloudServiceGrpc.CloudServiceBlockingStub.class,
                CloudServiceGrpc::newBlockingStub);
        FolderServiceGrpc.FolderServiceBlockingStub folderService = factory.create(FolderServiceGrpc.FolderServiceBlockingStub.class,
                FolderServiceGrpc::newBlockingStub);

        CloudServiceOuterClass.ListCloudsRequest builder = CloudServiceOuterClass.ListCloudsRequest.newBuilder().build();
        CloudServiceOuterClass.ListCloudsResponse clouds = cloudService.list(builder);
        for (CloudOuterClass.Cloud cloud : clouds.getCloudsList()) {
            FolderServiceOuterClass.ListFoldersRequest listFoldersRequest = FolderServiceOuterClass.ListFoldersRequest.newBuilder().setCloudId(cloud.getId()).build();

            try {
                FolderServiceOuterClass.ListFoldersResponse listFoldersResponse = folderService.list(listFoldersRequest);
                for (FolderOuterClass.Folder folder : listFoldersResponse.getFoldersList()) {
                    FunctionServiceOuterClass.ListFunctionsRequest request = FunctionServiceOuterClass.ListFunctionsRequest.newBuilder().setFolderId(folder.getId()).build();
                    FunctionServiceOuterClass.ListFunctionsResponse list = funcService.list(request);
                    List<Function> functionsList = list.getFunctionsList();
                    for (Function f :
                            functionsList) {
                        System.out.printf("%s <%s>\n\tStatus: %s\n\tCloud: %s[%s],\n\tFolder: %s[%s]\n\n"
                                , f.getName()
                                , f.getId()
                                , f.getStatus()
                                , cloud.getName()
                                , cloud.getId()
                                , folder.getName()
                                , folder.getId());
                    }
                }
            } catch (io.grpc.StatusRuntimeException e) {
                if (e.getMessage().equals("PERMISSION_DENIED: You are not authorized for this operation.")) {
                    logger.info("Cant get folder on cloud " + cloud.getName() + ". Permission denied");
                    return;
                } else {
                    throw new GradleException(e.toString());
                }

            }
        }

    }
}
