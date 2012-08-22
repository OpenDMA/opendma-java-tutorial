package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaPropertyInfoEnumeration;

public class Lession6_SimplePrintObjectProperties
{

    public static void main(String[] args)
    {
        Lession6_SimplePrintObjectProperties lession1 = new Lession6_SimplePrintObjectProperties();
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
            OdmaRepository repo = session.getRepository(repoId);
            
            // print out repository
            printObjectProperties(repo);
            
            // print inheritance hierarchy
            System.out.println();
            System.out.println("Inheritance hierarchy:");
            OdmaClass clazz = repo.getOdmaClass();
            while(clazz != null)
            {
                System.out.println(clazz.getQName());
                clazz = clazz.getParent();
            }
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }
    
    public void printObjectProperties(OdmaObject obj) throws Exception
    {
        System.out.println("Object " + obj.getId() + " of class " + obj.getOdmaClass().getQName());
        // get the class of the object
        OdmaClass cls = obj.getOdmaClass();
        // get enumeration of all properties
        OdmaPropertyInfoEnumeration propInfos = cls.getProperties();
        // iterate over all PropertyInfos
        Iterator<?> itPropInfos = propInfos.iterator();
        while(itPropInfos.hasNext())
        {
            OdmaPropertyInfo propInfo =
                (OdmaPropertyInfo)itPropInfos.next();
            OdmaQName propertyName = propInfo.getQName();
            int dataType = propInfo.getDataType().intValue();
            boolean multiValue = propInfo.getMultiValue().booleanValue();
            // get the property from the object
            OdmaProperty prop = obj.getProperty(propertyName);
            // print property value
            System.out.print("    ");
            System.out.print(propertyName);
            System.out.print(" (" + dataType + ")");
            System.out.print(" [" + (multiValue ? "m" : "s" ) + "] : ");
            if(!multiValue)
            {
                Object value = prop.getValue();
                if(value == null)
                {
                    System.out.println("<null>");
                }
                else
                {
                    System.out.println(value.toString());
                }
            }
            else
            {
                System.out.println("multivalued");
            }
        }
    }

}
