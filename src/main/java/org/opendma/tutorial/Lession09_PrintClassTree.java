package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

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

        // get Session
        XmlRepositorySessionProvider sessionProvider = new XmlRepositorySessionProvider();
        sessionProvider.setClasspathResource("SampleRepository.xml");
        OdmaSession session = sessionProvider.getSession();

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
        Iterable<OdmaClass> subClasses = cls.getSubClasses();
        if(subClasses != null)
        {
            Iterator<OdmaClass> itSubClasses = subClasses.iterator();
            while(itSubClasses.hasNext())
            {
                iterativePrintClassAndSubclasses(itSubClasses.next(),indent+1);
            }
        }
    }

}
