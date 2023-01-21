/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.internal;

import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.jvm.tasks.Jar;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

public class ElsaInternalJavaDecorationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        var configDir = findConfigDir(target.getProjectDir());
        var file = new File(configDir, "config.properties");
        if(!file.exists()){
            var file2  = new File(configDir, "config.properties.template");
            if(!file2.exists()) {
                throw new IllegalStateException("file config/config.properties or config/config.properties.template must exist");
            }
            try {
                Files.copy(file2.toPath(), file.toPath());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        var props = new Properties();
        try {
            props.load(new StringReader(Files.readString(file.toPath(), StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        target.getRepositories().add(target.getRepositories().mavenCentral());
        target.getPlugins().apply("java-library");
        target.getPlugins().apply("maven-publish");
        target.setProperty("group", "com.gridnine.elsa");
        target.setProperty("version", props.getProperty("version") == null? "0.0.1" : props.getProperty("version"));
        target.getExtensions().configure("java", (JavaPluginExtension ext) ->{
            ext.setSourceCompatibility(JavaVersion.VERSION_17);
            ext.setTargetCompatibility(JavaVersion.VERSION_17);
            ext.withSourcesJar();
        });
        var ext = (ElsaInternalJavaExtension) target.getExtensions().findByName("elsa-internal-java-configuration");
        if(ext != null) {
            var publishing = (PublishingExtension) target.getExtensions().findByName("publishing");
            assert publishing != null;
            var mp = publishing.getPublications().create("mavenJava", MavenPublication.class);
            mp.from(target.getComponents().findByName("java"));
            mp.setArtifactId(ext.getArtefactId());
            publishing.getRepositories().maven(mavenArtifactRepository -> {
                mavenArtifactRepository.getCredentials().setUsername(props.getProperty("maven.username"));
                mavenArtifactRepository.getCredentials().setPassword(props.getProperty("maven.password"));
                mavenArtifactRepository.setAllowInsecureProtocol(true);
                try {
                    mavenArtifactRepository.setUrl(new URI(props.getProperty("maven.url")));
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("bad parameter maven.url", e);
                }
            });
        }
        try {
            target.getTasks().getByName("jar").setEnabled(true);
        } catch (Exception e){
            //noops
        }
        try {
            target.getTasks().getByName("bootJar").setEnabled(false);
        } catch (Exception e){
            //noops
        }
        try {
            target.getTasks().getByName("sourcesJar", (task) -> ((Jar) task).setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE));
        } catch (Exception e){
            //noops
        }
    }

    private File findConfigDir(File dir) {
        if(dir == null){
            return null;
        }
        var file =new File(dir, "config");
        if(file.exists()){
            return file;
        }
        return findConfigDir(dir.getParentFile());
    }


}
