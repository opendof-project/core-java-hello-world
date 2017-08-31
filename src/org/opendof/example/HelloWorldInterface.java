package org.opendof.example;

/**
 * Created by jreber on 7/13/2017.
 */

import org.opendof.core.oal.DOFInterface;
import org.opendof.core.oal.DOFInterfaceID;
import org.opendof.core.oal.DOFType;
import org.opendof.core.oal.value.DOFString;

/**
 * <b> Hello World </b><p>
 * Hello World! This interface has a single read-only property containing the popular phrase. <p>
 *
 * IID: [1:{00010000}] <p>
 *
 * <b> Properties </b><p>
 * Phrase
 * The phrase.
 * <ul>
 * <li> Read: true </li>
 * <li> Write: false </li>
 * </ul>
 */
public class HelloWorldInterface {

    /**
     * <b> phrase </b><p>
     * A phrase.
     */
    public static final DOFType phrase = new DOFString.Type(106, 256);

    public static final int PHRASE_ITEM_ID = 0;

    /**
     *  Hello World InterfaceID
     */
    public static final DOFInterfaceID INTERFACEID = DOFInterfaceID.create("[1:{00010000}]");


    /**
     *  Hello World Definition
     */
    public static final DOFInterface DEFINITION = new DOFInterface.Builder( INTERFACEID )
        .addProperty( PHRASE_ITEM_ID, false, true, phrase )
        .build();
}
