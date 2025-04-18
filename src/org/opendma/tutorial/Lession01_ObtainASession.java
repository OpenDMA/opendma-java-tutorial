package org.opendma.tutorial;

import java.util.Properties;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaSession;

public class Lession01_ObtainASession
{

    public static void main(String[] args)
    {
        Lession01_ObtainASession lession = new Lession01_ObtainASession();
        try
        {
            lession.run();
        }
        catch(Exception e)
        {
            System.out.println("Error executing tutorial code:");
            e.printStackTrace(System.out);
        }
    }
    
    private void run() throws Exception
    {

        // register Adaptor
        System.out.println("Registering Adaptor for the OpenDMA XML-Repository...");
        Class.forName("com.xaldon.opendma.xmlrepo.Adaptor");

        // get Session
        System.out.println("Trying to get a Session to the OpenDMA XML-Repository...");
        OdmaSession session = AdaptorManager.getSession("xmlrepo:SampleRepository.xml", "tutorial", "tutorialpw");
        try
        {
            // perform some work with this Session
            System.out.println("Performing some operations on session...");
        }
        finally
        {
            // always close the session
            session.close();
            System.out.println("Session closed.");
        }

        // get Session with additional parameters
        System.out.println("Trying to get another Session to the OpenDMA XML-Repository...");
        Properties prop = new Properties();
        prop.put("url", "xmlrepo:SampleRepository.xml");
        prop.put("user", "tutorial");
        prop.put("password", "tutorialpw");
        prop.put("locale", "de");
        prop.put("timezone", "GMT-10:00");
        OdmaSession session2 = AdaptorManager.getSession(prop);
        try
        {
            // perform some work with this Session
            System.out.println("Performing some operations on session...");
        }
        finally
        {
            // always close the session
            session2.close();
            System.out.println("Session closed.");
        }

    }

}
