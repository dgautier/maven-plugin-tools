package com.github.dgautier.maven.plugin;

import com.google.common.io.Files;
import com.xebialabs.overthere.OverthereFile;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by DGA on 08/01/2015.
 * @see "https://github.com/spring-projects/spring-boot/blob/master/spring-boot-tools/spring-boot-maven-plugin/src/main/java/org/springframework/boot/maven/RepackageMojo.java'
 */
@Mojo(name = "deployFile", defaultPhase = LifecyclePhase.DEPLOY)
public class CopyTargetMojo extends AbstractRemoteMojo {


    /**
     * The Maven project.
     *
     * @since 1.0
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Directory containing the generated archive.
     *
     * @since 1.0
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true)
    private File outputDirectory;

    /**
     * Name of the generated archive.
     *
     * @since 1.0
     */
    @Parameter(defaultValue = "${project.build.finalName}", required = true)
    private String finalName;


    /**
     * The directory on remote server the jar will be copied in.
     */
    @Parameter(property = "targetPath", required = true)
    private String targetPath;


    private OverthereFile remoteFile;

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException {
        super.execute();

        if (this.project.getPackaging().equals("pom")) {
            getLog().debug("repackage goal could not be applied to pom project.");
            return;
        }
    }


    protected void copyFileToDest() throws IOException {

        File target = getTargetFile();

        getLog().info("Copying File to : " + targetPath + target.getName());
        remoteFile = getConnection().getFile(targetPath + target.getName());

        Files.copy(target, remoteFile.getOutputStream());
        checkArgument(remoteFile.length() == target.length(), "Error while copying file, Input Size : " + target.length() + " Output Size : " + remoteFile.length());
    }


    private File getTargetFile() {
        return new File(this.outputDirectory, this.finalName + "."
                + this.project.getPackaging());
    }
    
    protected OverthereFile getRemoteFile() {
        return remoteFile;
    }


    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }


    @Override
    protected void onCheckPreconditions() {
        checkNotNull(targetPath, "targetPath must be set in plugin's configuration.");
        checkArgument(targetPath.endsWith("/"), "targetPath must be end with path separator");
    }
}
