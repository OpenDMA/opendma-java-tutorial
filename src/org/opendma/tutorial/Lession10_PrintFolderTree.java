package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaFolderEnumeration;

public class Lession10_PrintFolderTree
{

    public static void main(String[] args)
    {
        Lession10_PrintFolderTree lession1 = new Lession10_PrintFolderTree();
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
        OdmaFolderEnumeration subFolders = folder.getSubFolders();
        if(subFolders != null)
        {
            Iterator<?> itSubFolders = subFolders.iterator();
            while(itSubFolders.hasNext())
            {
                iterativePrintFolderTree((OdmaFolder)itSubFolders.next(),indent+1);
            }
        }
    }

}
