package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaClassEnumeration;

public class Lession09_PrintClassTree
{

    public static void main(String[] args)
    {
        Lession09_PrintClassTree lession = new Lession09_PrintClassTree();
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

            // print class tree
            iterativePrintClassAndSubclasses(repo.getRootClass(),0);
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

    protected void iterativePrintClassAndSubclasses(OdmaClass cls, int indent)
    {
        for(int i = 0; i < (indent*4); i++)
        {
            System.out.print(" ");
        }
        System.out.println(cls.getQName());
        OdmaClassEnumeration subClasses = cls.getSubClasses();
        if(subClasses != null)
        {
            Iterator<?> itSubClasses = subClasses.iterator();
            while(itSubClasses.hasNext())
            {
                iterativePrintClassAndSubclasses((OdmaClass)itSubClasses.next(),indent+1);
            }
        }
    }

}
