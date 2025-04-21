package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;
import org.opendma.api.OdmaType;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession06_SimplePrintObjectProperties
{

    public static void main(String[] args)
    {
        Lession06_SimplePrintObjectProperties lession = new Lession06_SimplePrintObjectProperties();
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

        // get Session
        XmlRepositorySessionProvider sessionProvider = new XmlRepositorySessionProvider();
        sessionProvider.setClasspathResource("SampleRepository.xml");
        OdmaSession session = sessionProvider.getSession();

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
                clazz = clazz.getSuperClass();
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
        Iterable<OdmaPropertyInfo> propInfos = cls.getProperties();
        // iterate over all PropertyInfos
        Iterator<OdmaPropertyInfo> itPropInfos = propInfos.iterator();
        while(itPropInfos.hasNext())
        {
            OdmaPropertyInfo propInfo = itPropInfos.next();
            OdmaQName propertyName = propInfo.getQName();
            OdmaType dataType = OdmaType.fromNumericId(propInfo.getDataType());
            boolean multiValue = propInfo.isMultiValue();
            // get the property from the object
            OdmaProperty prop = obj.getProperty(propertyName);
            // print property value
            System.out.print("    ");
            System.out.print(propertyName);
            System.out.print(" (" + dataType + ")");
            System.out.print(" [" + (multiValue ? "multi" : "single" ) + "] : ");
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
