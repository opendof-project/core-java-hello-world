package org.opendof.example;

import org.opendof.core.ReconnectingStateListener;
import org.opendof.core.oal.*;
import org.opendof.core.transport.inet.InetTransport;

public class HelloWorldProvider extends DOFObject.DefaultProvider{

    //Application configuration is hard-coded here for convenience

    //The host or IP address and port of the server
    private static final String HOST = "localhost";
    private static final int PORT = 3567;

    //Set the domain, identity, and key for the credentials here.
    //WARNING!!! Defining the credentials in code is not recommended. They are included in this example for convenience.
    public static final String DOMAIN = "[6:example.opendof.org]";
    public static final String IDENTITY = "[3:user@example.opendof.org]";
    private static final byte[] KEY = DOFUtil.hexStringToBytes("0000000000000000000000000000000000000000000000000000000000000000");

    //Set the provider object identifier here
    private static final String PROVIDER_OBJECT_ID = "[3:helloWorldProvider@example.opendof.org]";

    //A timeout of 30 seconds is plenty
    private static final int TIMEOUT = 30000;

    public static void main(String[] args) throws Exception{

        //Create a start the provider
        HelloWorldProvider helloWorldProvider = new HelloWorldProvider();
        helloWorldProvider.start();

        //Sleep forever (ctrl+c to exit)
        while(true) {
            try {
                Thread.sleep(Integer.MAX_VALUE);
            }
            catch(Exception e){
                //Ignored
            }
        }

    }

    private final DOF dof;

    private DOFConnection connection;
    private DOFSystem system;

    public HelloWorldProvider(){
        this.dof = new DOF(new DOF.Config.Builder().setName("HelloWorldRequestor").build());
    }

    public void start() throws Exception{
        DOFCredentials credentials = createCredentials();

        connection = createConnection(credentials, HOST, PORT);
        connection.addStateListener(new ReconnectingStateListener());

        system = createSystem(credentials);
        system.waitAuthorized(TIMEOUT);

        DOFObjectID providerID = DOFObjectID.create(PROVIDER_OBJECT_ID);
        DOFObject provider = system.createObject(providerID);

        provider.beginProvide(HelloWorldInterface.DEFINITION, this);
    }

    /**
     * Create the DOFCredentials that will be used for the application. These consist of the domain, identity, and a secret.
     * This application uses key credentials, so a 32-byte key is provided for the secret. The domain must be specified.
     * A user/device in one domain will not be able to access another domain unless they also have credentials in that domian.
     * The identity must be specified and consists of an authentication identifier.
     * @return The DOFCredentials object.
     */
    private DOFCredentials createCredentials(){
        DOFObjectID.Domain domainID = DOFObjectID.Domain.create(DOMAIN);
        DOFObjectID.Authentication identity = DOFObjectID.Authentication.create(IDENTITY);
        return DOFCredentials.Key.create(domainID, identity, KEY);
    }

    /**
     * Create a secure DOFConnection to the given host and port authenticated with the given credentials.
     * @param credentials The DOFCredentials.
     * @param host The host.
     * @param port The port.
     * @return The DOFConnection.
     */
    private DOFConnection createConnection(DOFCredentials credentials, String host, int port){
        DOFAddress address = InetTransport.createAddress(host, port);
        DOFConnection.Config config = new DOFConnection.Config.Builder(DOFConnection.Type.STREAM, address)
                .setCredentials(credentials)
                .build();

        return dof.createConnection(config);
    }

    /**
     * Create a DOFSystem associated with the given credentials. This is a logical partition on the local node (called a DOF).
     * @param credentials The credentials
     * @return The DOFSystem.
     */
    private DOFSystem createSystem(DOFCredentials credentials){
        DOFSystem.Config config = new DOFSystem.Config.Builder()
                .setCredentials(credentials)
                .build();
        return dof.createSystem(config);
    }

}
