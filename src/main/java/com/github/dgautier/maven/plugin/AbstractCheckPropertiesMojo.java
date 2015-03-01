package com.github.dgautier.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Created by DGA on 08/01/2015.
 */
public abstract class AbstractCheckPropertiesMojo extends AbstractMojo {

    protected abstract void onCheckPreconditions();


    @Override
    public void execute() throws MojoExecutionException {
        onCheckPreconditions();
    }
}
