package com.github.dgautier.maven.plugin;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.ssh.SshConnectionType;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.ssh.SshConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.ssh.SshConnectionType.SFTP_WINSSHD;

/**
 * Created by DGA on 08/01/2015.
 */
public abstract class AbstractRemoteMojo extends AbstractCheckPropertiesMojo {


    /**
     * Remote Server name/address hosting IBM Content Navigator
     */
    @Parameter(property = "hostname", required = true)
    private String hostName;

    /**
     * Remote Operating System login User
     */
    @Parameter(property = "user", required = true)
    private String userName;

    /**
     * Remote Operating System user password
     */
    @Parameter(property = "password", required = true)
    private String password;

    /**
     * Remote Operating System (TODO do something depending on operating system)
     * Possible Values :  WINDOWS, UNIX
     */
    @Parameter(property = "operatingSystem", required = true)
    private String operatingSystem;

    /**
     * @see com.xebialabs.overthere.ssh.SshConnectionType
     */
    @Parameter(property = "connectionType", required = true)
    private String connectionType;

    private OverthereConnection connection;

    protected OverthereConnection getConnection() {
        return connection;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException {
        super.execute();
        initConnection();
    }


    protected void onCheckPreconditions() {
        checkNotNull(hostName, "Remote hostname must be set in plugin's configuration.");
        checkNotNull(userName, "Remote Connection userName must be set in plugin's configuration.");
        checkNotNull(password, "Remote Connection password must be set in plugin's configuration.");
        checkNotNull(operatingSystem, "operatingSystem must be set in plugin's configuration.");
        checkNotNull(connectionType, "connectionType must be set in plugin's configuration.");
    }


    protected void initConnection() {
        ConnectionOptions options = new ConnectionOptions();
        options.set(ADDRESS, hostName);
        options.set(USERNAME, userName);
        options.set(PASSWORD, password);
        options.set(OPERATING_SYSTEM, operatingSystem);

        if (SshConnectionType.SFTP_WINSSHD.equals(connectionType)) {
            getLog().debug("Connection Type Implemented : " + connectionType);
            options.set(CONNECTION_TYPE, SFTP_WINSSHD);
        } else if (SshConnectionType.SFTP.equals(connectionType)) {
            getLog().debug("Connection Type Implemented : " + connectionType);
            options.set(CONNECTION_TYPE, connectionType);
        } else {
            getLog().debug("Connection Type not implemented : " + connectionType);
            options.set(CONNECTION_TYPE, connectionType);
        }

        connection = Overthere.getConnection("ssh", options);
    }


    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    protected String getHostName() {
        return this.hostName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }


    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
}
