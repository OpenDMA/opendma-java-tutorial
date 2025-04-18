package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaSession;

public class Lession04_RuntimeTypeInformation
{

    public static void main(String[] args)
    {
        Lession04_RuntimeTypeInformation lession = new Lession04_RuntimeTypeInformation();
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
        Class.forName("com.xaldon.opendma.xmlrepo.Adaptor");

        // get Session
        OdmaSession session = AdaptorManager.getSession("xmlrepo:SampleRepository.xml", "tutorial", "tutorialpw");
        try
        {

            // get the repository by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaObject repo = session.getRepository(repoId);
            
            // get the opendma:Class of the repository
            OdmaObject cls = repo.getProperty(OdmaCommonNames.PROPERTY_CLASS).getReference();
            
            // print out information about the class of the repository
            System.out.println("Class of the repository object:");
            System.out.println("    Name: " + cls.getProperty(OdmaCommonNames.PROPERTY_NAME).getString() );
            System.out.println("    ID: " + cls.getProperty(OdmaCommonNames.PROPERTY_ID).getId().toString() );
            System.out.println("    Instantiable: " + cls.getProperty(OdmaCommonNames.PROPERTY_INSTANTIABLE).getBoolean().toString() );

            // get set of PropertyInfos
            Iterable<? extends OdmaObject> propInfos = cls.getProperty(OdmaCommonNames.PROPERTY_PROPERTIES).getReferenceIterable();
            
            // print out all PropertyInfos
            System.out.println("Repository object contains the following properties:");
            Iterator<? extends OdmaObject> itPropInfos = propInfos.iterator();
            while(itPropInfos.hasNext())
            {
                OdmaObject propInfo = itPropInfos.next();
                System.out.println("    " + propInfo.getProperty(OdmaCommonNames.PROPERTY_NAME).getString());
            }
            
            // get the parent
            OdmaObject parentCls = cls.getProperty(OdmaCommonNames.PROPERTY_PARENT).getReference();
            System.out.println("Parent: " + parentCls.getProperty(OdmaCommonNames.PROPERTY_NAME).getString());
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

}
