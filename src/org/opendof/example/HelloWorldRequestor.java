/*
**  Copyright (c) 2010-2017, Panasonic Corporation.
**
**  Permission to use, copy, modify, and/or distribute this software for any
**  purpose with or without fee is hereby granted, provided that the above
**  copyright notice and this permission notice appear in all copies.
**
**  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
**  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
**  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
**  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
**  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
**  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
**  OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/

package org.opendof.example;

import org.opendof.core.oal.*;
import org.opendof.core.transport.inet.InetTransport;

public class HelloWorldRequestor {

    private static final String HOST = "localhost";
    private static final int PORT = 3567;

    public static final String DOMAIN = "[6:example.opendof.org]";
    public static final String IDENTITY = "[3:user@example.opendof.org]";
    private static final byte[] KEY = DOFUtil.hexStringToBytes("0000000000000000000000000000000000000000000000000000000000000000");

    private static final String PROVIDER_OBJECT_ID = "[3:helloWorldProvider@example.opendof.org]";

    private static final int TIMEOUT = 30000;

    public static void main(String[] args){
        HelloWorldRequestor helloWorldRequestor = new HelloWorldRequestor();
        try {
            helloWorldRequestor.run();
        } catch (DOFException e) {
            System.out.println("Failed: " + e);
        }
        helloWorldRequestor.destroy();
    }

    private final DOF dof;

    private DOFConnection connection;
    private DOFSystem system;

    public HelloWorldRequestor(){
        this.dof = new DOF(new DOF.Config.Builder().setName("HelloWorldRequestor").build());
    }

    public void run() throws DOFException{
        DOFCredentials credentials = createCredentials();

        connection = createConnection(credentials, HOST, PORT);
        connection.connect(TIMEOUT);

        System.out.println("Connection successful");

        system = createSystem(credentials);
        system.waitAuthorized(TIMEOUT);

        System.out.println("System creation successful");

        DOFObjectID providerID = DOFObjectID.create(PROVIDER_OBJECT_ID);
        system.beginInterest(providerID, HelloWorldInterface.INTERFACEID, DOFInterestLevel.WATCH);

        DOFObject requestor = system.waitProvider(providerID, HelloWorldInterface.INTERFACEID, TIMEOUT);

        System.out.println("Wait provider successful");

        DOFResult result = requestor.get(HelloWorldInterface.DEFINITION.getProperty(HelloWorldInterface.PHRASE_ITEM_ID), TIMEOUT);

        System.out.println("Received: " + result.asString());
    }

    public void destroy(){
        dof.destroy();
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
