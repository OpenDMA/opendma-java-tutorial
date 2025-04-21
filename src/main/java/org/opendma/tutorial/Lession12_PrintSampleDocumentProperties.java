package org.opendma.tutorial;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaGuid;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaSession;
import org.opendma.api.OdmaType;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession12_PrintSampleDocumentProperties
{

    public static void main(String[] args)
    {
        Lession12_PrintSampleDocumentProperties lession = new Lession12_PrintSampleDocumentProperties();
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

            // get the SampleDocument by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaId sampleDocumentId = new OdmaId("sample-document-a1");
            OdmaObject obj = session.getObject(repoId, sampleDocumentId, null);
            
            // print out properties of Sample Document
            printObjectProperties(obj);
            
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
                System.out.println(convertDataValue(dataType, prop.getValue()));
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
                    if(dataType == OdmaType.REFERENCE)
                    {
                        @SuppressWarnings("unchecked")
						Iterable<OdmaObject> objEnum = (Iterable<OdmaObject>)value;
                        Iterator<OdmaObject> itObjects = objEnum.iterator();
                        while(itObjects.hasNext())
                        {
                            OdmaObject referencedObject = itObjects.next();
                            System.out.println("        "+convertDataValue(dataType, referencedObject));
                        }
                    }
                    else
                    {
                        List<?> dataList = (List<?>)value;
                        for(Object val : dataList)
                        {
                            System.out.println("        "+convertDataValue(dataType, val));
                        }
                    }
                }
            }
        }
        
    }
    
    protected static String convertDataValue(OdmaType type, Object value)
    {
        if(value == null)
        {
            return "<null>";
        }
        switch(type)
        {
        case STRING:
            return (String)value;
        case INTEGER:
            return ((Integer)value).toString();
        case SHORT:
            return ((Short)value).toString();
        case LONG:
            return ((Long)value).toString();
        case FLOAT:
            return ((Float)value).toString();
        case DOUBLE:
            return ((Double)value).toString();
        case BOOLEAN:
            return ((Boolean)value).toString();
        case DATETIME:
            return ((Date)value).toString();
        case BLOB:
            return Integer.toString(((byte[])value).length) + " octets of data";
        case REFERENCE:
            OdmaObject referencedObject = (OdmaObject)value;
            return referencedObject.getId().toString() + " of class " + referencedObject.getOdmaClass().getQName();
        case CONTENT:
            return "content-stream";
        case ID:
            return ((OdmaId)value).toString();
        case GUID:
            return ((OdmaGuid)value).toString();
        default:
            return "error-converting-value";
        }
    }

}
