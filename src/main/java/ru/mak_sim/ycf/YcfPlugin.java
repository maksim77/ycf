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
