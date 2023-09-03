package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaAssociation;
import org.opendma.api.OdmaContainable;
import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.collections.OdmaAssociationEnumeration;
import org.opendma.api.collections.OdmaFolderEnumeration;

public class Lession11_PrintFolderContent
{

    public static void main(String[] args)
    {
        Lession11_PrintFolderContent lession = new Lession11_PrintFolderContent();
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

            // print inheritance tree
            if(repo.getRootFolder() != null)
            {
                iterativePrintFolderContent(repo.getRootFolder(),0);
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

    protected void iterativePrintFolderContent(OdmaFolder folder, int indent)
    {
        StringBuffer indentStr = new StringBuffer(indent*4);
        for(int i = 0; i < (indent*4); i++)
        {
            indentStr.append(" ");
        }
        System.out.println(indentStr.toString() + "-" + folder.getTitle());
        OdmaAssociationEnumeration associations = folder.getAssociations();
        if(associations != null)
        {
            Iterator<?> itAssociations = associations.iterator();
            while(itAssociations.hasNext())
            {
                OdmaAssociation assoc = (OdmaAssociation)itAssociations.next();
                System.out.print(indentStr.toString() +
                                 "    =" +
                                 assoc.getName());
                OdmaContainable containee = assoc.getContainable();
                System.out.println(" --> ID " + containee.getId() + " [" + containee.getOdmaClass().getQName() + "]");
            }
        }
        OdmaFolderEnumeration subFolders = folder.getSubFolders();
        if(subFolders != null)
        {
            Iterator<?> itSubFolders = subFolders.iterator();
            while(itSubFolders.hasNext())
            {
                iterativePrintFolderContent((OdmaFolder)itSubFolders.next(),indent+1);
            }
        }
    }

}
