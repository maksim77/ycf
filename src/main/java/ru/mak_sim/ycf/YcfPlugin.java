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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class YcfPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getExtensions().create("ycf", YcfPluginExtension.class);
        project.getPluginManager().apply("java-library-distribution");

        project.getTasks().register("ycfList", YcfTaskList.class);
        project.getTasks().register("ycfCreateVersion", YcfTaskCreateVersion.class);
    }
}
