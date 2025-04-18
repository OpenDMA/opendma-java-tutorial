package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;

public class Lession05_ClassDependentInterfaces
{

    public static void main(String[] args)
    {
        Lession05_ClassDependentInterfaces lession = new Lession05_ClassDependentInterfaces();
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
            System.out.println("Instantiable: " + cls.isInstantiable());
            System.out.println("Hidden: " + cls.isHidden());
            System.out.println("System: " + cls.isSystem());

            // get set of PropertyInfos
            Iterable<OdmaPropertyInfo> propInfos = cls.getProperties();
            
            // print out all PropertyInfos
            System.out.println("Repository object contains the following properties:");
            Iterator<OdmaPropertyInfo> itPropInfos = propInfos.iterator();
            while(itPropInfos.hasNext())
            {
                OdmaPropertyInfo propInfo = itPropInfos.next();
                System.out.println(propInfo.getQName());
                System.out.println("    DataType: " + propInfo.getDataType());
                System.out.println("    MultiValue: " + propInfo.isMultiValue());
                System.out.println("    ReadOnly: " + propInfo.isReadOnly());
            }
            
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

}
