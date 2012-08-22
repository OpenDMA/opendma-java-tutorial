package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.OdmaTypes;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.collections.OdmaObjectEnumeration;

public class Lession4_RuntimeTypeInformation
{

    public static void main(String[] args)
    {
        Lession4_RuntimeTypeInformation lession1 = new Lession4_RuntimeTypeInformation();
        try
        {
            lession1.run();
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
        OdmaSession session =
            AdaptorManager.getSession("xmlrepo:SampleRepository.xml", "tutorial", "tutorialpw");
        try
        {

            // get the repository by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaObject repo = session.getRepository(repoId);
            
            // get the opendma:Class of the repository
            OdmaObject cls = repo.getProperty(OdmaTypes.PROPERTY_CLASS)
                                 .getReference();
            
            // print out information about the class of the repository
            System.out.println("Class of the repository object:");
            System.out.println("    Name: " + cls.getProperty(OdmaTypes.PROPERTY_NAME).getString() );
            System.out.println("    ID: " + cls.getProperty(OdmaTypes.PROPERTY_ID).getId().toString() );
            System.out.println("    Instantiable: " + cls.getProperty(OdmaTypes.PROPERTY_INSTANTIABLE).getBoolean().toString() );

            // get set of PropertyInfos
            OdmaObjectEnumeration propInfos = cls.getProperty(OdmaTypes.PROPERTY_PROPERTIES).getReferenceEnumeration();
            
            // print out all PropertyInfos
            System.out.println("Repository object contains the following properties:");
            Iterator<?> itPropInfos = propInfos.iterator();
            while(itPropInfos.hasNext())
            {
                OdmaObject propInfo = (OdmaObject)itPropInfos.next();
                System.out.println("    " + propInfo.getProperty(OdmaTypes.PROPERTY_NAME).getString());
            }
            
            // get the parent
            OdmaObject parentCls = cls.getProperty(OdmaTypes.PROPERTY_PARENT).getReference();
            System.out.println("Parent: " + parentCls.getProperty(OdmaTypes.PROPERTY_NAME).getString());
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

}
