package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaPropertyInfoEnumeration;

public class Lession08_InheritancePath
{

    public static void main(String[] args)
    {
        Lession08_InheritancePath lession = new Lession08_InheritancePath();
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
        OdmaSession session =
            AdaptorManager.getSession("xmlrepo:SampleRepository.xml", "tutorial", "tutorialpw");
        try
        {

            // get the repository by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaRepository repo = session.getRepository(repoId);

            // print inheritance hierarchy
            System.out.println("Inheritance hierarchy:");
            OdmaClass clazz = repo.getOdmaClass();
            while(clazz != null)
            {
                // print the qualified name of this class
                System.out.println(clazz.getQName());
                // get enumeration of declared properties
                OdmaPropertyInfoEnumeration propInfos =
                    clazz.getDeclaredProperties();
                // iterate over declared PropertyInfos
                Iterator<?> itPropInfos = propInfos.iterator();
                while(itPropInfos.hasNext())
                {
                    OdmaPropertyInfo propInfo =
                        (OdmaPropertyInfo)itPropInfos.next();
                    System.out.println("    "+propInfo.getQName());
                }
                // get next class in hierarchy path
                clazz = clazz.getParent();
            }
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

}
