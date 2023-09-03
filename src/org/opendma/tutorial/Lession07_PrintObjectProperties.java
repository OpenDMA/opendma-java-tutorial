package org.opendma.tutorial;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.OdmaTypes;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaObjectEnumeration;
import org.opendma.api.collections.OdmaPropertyInfoEnumeration;

public class Lession07_PrintObjectProperties
{

    public static void main(String[] args)
    {
        Lession07_PrintObjectProperties lession = new Lession07_PrintObjectProperties();
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
            boolean multiValue = propInfo.getMultiValue().booleanValue();
            // get the property from the object
            OdmaProperty prop = obj.getProperty(propertyName);
            // print property value
            System.out.print("    ");
            System.out.print(propertyName);
            System.out.print(" (" + getDataTypeString(propInfo.getDataType()) + ")");
            System.out.print(" [" + (multiValue ? "multi" : "single" ) + "] : ");
            if(!multiValue)
            {
                System.out.println(convertDataValue(propInfo.getDataType(), prop.getValue()));
            }
            else
            {
                Object value = prop.getValue();
                if(value == null)
                {
                    System.out.println("empty");
                }
                else
                {
                    System.out.println();
                    if(propInfo.getDataType().intValue() == OdmaTypes.TYPE_REFERENCE)
                    {
                        OdmaObjectEnumeration objEnum = (OdmaObjectEnumeration)value;
                        Iterator<?> itObjects = objEnum.iterator();
                        while(itObjects.hasNext())
                        {
                            OdmaObject referencedObject = (OdmaObject)itObjects.next();
                            System.out.println("        "+convertDataValue(propInfo.getDataType(), referencedObject));
                        }
                    }
                    else
                    {
                        List<?> dataList = (List<?>)value;
                        for(Object val : dataList)
                        {
                            System.out.println("        "+convertDataValue(propInfo.getDataType(), val));
                        }
                    }
                }
            }
        }
        
    }
    
    public static String getDataTypeString(Integer type)
    {
        String s = dataTypeMap.get(type);
        if(s == null)
        {
            s = "<unknown("+type.toString()+")>";
        }
        return s;
    }
    
    protected static String convertDataValue(Integer type, Object value)
    {
        if(value == null)
        {
            return "<null>";
        }
        switch(type.intValue())
        {
        case OdmaTypes.TYPE_STRING:
            return (String)value;
        case OdmaTypes.TYPE_INTEGER:
            return ((Integer)value).toString();
        case OdmaTypes.TYPE_SHORT:
            return ((Short)value).toString();
        case OdmaTypes.TYPE_LONG:
            return ((Long)value).toString();
        case OdmaTypes.TYPE_FLOAT:
            return ((Float)value).toString();
        case OdmaTypes.TYPE_DOUBLE:
            return ((Double)value).toString();
        case OdmaTypes.TYPE_BOOLEAN:
            return ((Boolean)value).toString();
        case OdmaTypes.TYPE_DATETIME:
            return ((Date)value).toString();
        case OdmaTypes.TYPE_BLOB:
            return Integer.toString(((byte[])value).length) + " octets of data";
        case OdmaTypes.TYPE_REFERENCE:
            OdmaObject referencedObject = (OdmaObject)value;
            return referencedObject.getId().toString() + " of class " + referencedObject.getOdmaClass().getQName();
        case OdmaTypes.TYPE_CONTENT:
            return "content-stream";
        case OdmaTypes.TYPE_ID:
            return ((OdmaId)value).toString();
        case OdmaTypes.TYPE_GUID:
            return ((OdmaGuid)value).toString();
        default:
            return "error-converting-value";
        }
    }
    
    protected static Map<Integer,String> dataTypeMap = new HashMap<Integer,String>();
    
    static
    {
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_STRING),"STRING");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_INTEGER),"INTEGER");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_SHORT),"SHORT");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_LONG),"LONG");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_FLOAT),"FLOAT");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_DOUBLE),"DOUBLE");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_BOOLEAN),"BOOLEAN");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_DATETIME),"DATETIME");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_BLOB),"BLOB");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_REFERENCE),"REFERENCE");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_CONTENT),"CONTENT");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_ID),"ID");
        dataTypeMap.put(Integer.valueOf(OdmaTypes.TYPE_GUID),"GUID");
    }

}
