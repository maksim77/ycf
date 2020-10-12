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
