package org.opendma.tutorial;

import java.util.Properties;

import org.opendma.api.OdmaAdaptor;
import org.opendma.api.OdmaAdaptorDiscovery;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

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
        
        // ----- Option 1: OdmaSessionProvider -----

        // instantiate and configure OdmaSessionProvider
        System.out.println("Configuring a new session provider for the OpenDMA XML-Repository...");
        XmlRepositorySessionProvider sessionProvider = new XmlRepositorySessionProvider();
        sessionProvider.setClasspathResource("SampleRepository.xml");

        // get Session
        System.out.println("Trying to get a Session to the OpenDMA XML-Repository from the session provider...");
        OdmaSession session = sessionProvider.getSession();
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
        
        // ----- Option 2: OdmaAdaptorDiscovery -----
        
        // instantiate a new OdmaAdaptorDiscovery and try to find a suitable adaptor
        System.out.println("Discovering the OpenDMA XML-Repository adaptor...");
        OdmaAdaptorDiscovery adaptorDiscovery = new OdmaAdaptorDiscovery();
        for(OdmaAdaptor adaptor : adaptorDiscovery.getAllAdaptors())
        {
            System.out.println("  "+adaptor.getSystemId());
        }
        OdmaAdaptor xmlRepoAdaptor = adaptorDiscovery.getAdaptor("xmlrepo");
        if(xmlRepoAdaptor == null)
        {
            System.err.println("Cannot find suitable adaptor.");
            return;
        }

        // get Session with additional parameters
        System.out.println("Trying to get another Session to the OpenDMA XML-Repository from the adaptor...");
        Properties prop = new Properties();
        prop.put("classpathResource", "SampleRepository.xml");
        prop.put("enforceRequired", "false");
        // note: the following properties are ignored by the xml repository. They are just shown here for illustrative purposes
        prop.put("user", "tutorial");
        prop.put("password", "tutorialpw");
        prop.put("locale", "de");
        prop.put("timezone", "GMT-10:00");
        OdmaSession session2 = xmlRepoAdaptor.connect(prop);
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
