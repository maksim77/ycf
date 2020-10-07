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
