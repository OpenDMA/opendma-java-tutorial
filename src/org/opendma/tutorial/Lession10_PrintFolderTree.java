package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession10_PrintFolderTree
{

    public static void main(String[] args)
    {
        Lession10_PrintFolderTree lession = new Lession10_PrintFolderTree();
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

            // print inheritance tree
            if(repo.getRootFolder() != null)
            {
                iterativePrintFolderTree(repo.getRootFolder(),0);
            }
            else
            {
                System.out.println("This repository does not have a folder tree.");
            }
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

    protected void iterativePrintFolderTree(OdmaFolder folder, int indent)
    {
        for(int i = 0; i < (indent*4); i++)
        {
            System.out.print(" ");
        }
        System.out.println(folder.getTitle());
        Iterable<OdmaFolder> subFolders = folder.getSubFolders();
        if(subFolders != null)
        {
            Iterator<OdmaFolder> itSubFolders = subFolders.iterator();
            while(itSubFolders.hasNext())
            {
                iterativePrintFolderTree(itSubFolders.next(),indent+1);
            }
        }
    }

}
