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

import java.util.HashMap;
import java.util.List;

class YcfPluginExtension {
    public static final String RUNTIME = "java11";

    String token;

    String function_id;
    String description;

    String runtime = RUNTIME;
    String entrypoint;
    Long memory = 134217728L;
    Long execution_timeout;
    HashMap<String, String> environment;
    List<String> tags;

    public YcfPluginExtension() {
    }
}
