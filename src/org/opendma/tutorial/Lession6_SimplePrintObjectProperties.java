package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.OdmaTypes;
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
            
            // print out repository info
            System.out.println("Repository:");
            System.out.println("ID: " + repo.getId());
            System.out.println("Name: " + repo.getName());
            System.out.println("DispName: " + repo.getDisplayName());
            
            // get the Class of the Repository
            OdmaClass cls = repo.getOdmaClass();
            
            // print out information about the class of the repository
            System.out.println("Class of the repository object:");
            System.out.println("ID: " + cls.getId());
            System.out.println("Name: " + cls.getName());
            System.out.println("DispName: " + cls.getDisplayName());
            System.out.println("Instantiable: " + cls.getInstantiable());
            System.out.println("Hidden: " + cls.getHidden());
            System.out.println("System: " + cls.getSystem());

            // get set of PropertyInfos
            OdmaPropertyInfoEnumeration propInfos = cls.getProperties();
            
            // print out all PropertyInfos
            System.out.println("Repository object contains the following properties:");
            Iterator<?> itPropInfos = propInfos.iterator();
            while(itPropInfos.hasNext())
            {
                OdmaPropertyInfo propInfo = (OdmaPropertyInfo)itPropInfos.next();
                System.out.println(propInfo.getQName());
                System.out.println("    DataType: " + propInfo.getDataType());
                System.out.println("    MultiValue: " + propInfo.getMultiValue());
                System.out.println("    ReadOnly: " + propInfo.getReadOnly());
            }
            
            // print class hierarchy
            System.out.println("Class hierarchy:");
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
        // get the class of the object
        OdmaClass cls = obj.getOdmaClass();
        // get enumeration of all properties
        OdmaPropertyInfoEnumeration propInfos = cls.getProperties();
        // iterate over all PropertyInfos
        Iterator<?> itPropInfos = propInfos.iterator();
        while(itPropInfos.hasNext())
        {
            OdmaPropertyInfo propInfo = (OdmaPropertyInfo)itPropInfos.next();
            OdmaQName propertyName = propInfo.getQName();
            // get the property from the object
            OdmaProperty prop = obj.getProperty(propertyName);
            // print property value
            switch(propInfo.getDataType().intValue())
            {
            case OdmaTypes.TYPE_STRING:
                System.out.println(propertyName + " (STRING): " + prop.getString());
                break;
            case OdmaTypes.TYPE_INTEGER:
                System.out.println(propertyName + " (INTEGER): " + prop.getString());
                break;
            case OdmaTypes.TYPE_SHORT:
                System.out.println(propertyName + " (SHORT): " + prop.getString());
                break;
            case OdmaTypes.TYPE_LONG:
                System.out.println(propertyName + " (LONG): " + prop.getString());
                break;
            case OdmaTypes.TYPE_FLOAT:
                System.out.println(propertyName + " (FLOAT): " + prop.getString());
                break;
            case OdmaTypes.TYPE_DOUBLE:
                System.out.println(propertyName + " (DOUBLE): " + prop.getString());
                break;
            case OdmaTypes.TYPE_BOOLEAN:
                System.out.println(propertyName + " (BOOLEAN): " + prop.getString());
                break;
            case OdmaTypes.TYPE_DATETIME:
                System.out.println(propertyName + " (DATETIME): " + prop.getString());
                break;
            case OdmaTypes.TYPE_BLOB:
                System.out.println(propertyName + " (BLOB): " + prop.getString());
                break;
            case OdmaTypes.TYPE_REFERENCE:
                System.out.println(propertyName + " (REFERENCE): " + prop.getString());
                break;
            case OdmaTypes.TYPE_CONTENT:
                System.out.println(propertyName + " (CONTENT): " + prop.getString());
                break;
            case OdmaTypes.TYPE_ID:
                System.out.println(propertyName + " (ID): " + prop.getString());
                break;
            case OdmaTypes.TYPE_GUID:
                System.out.println(propertyName + " (GUID): " + prop.getString());
                break;
            }
        }
        
    }

}
