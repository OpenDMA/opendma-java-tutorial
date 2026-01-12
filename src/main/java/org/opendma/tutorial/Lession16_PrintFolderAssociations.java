package org.opendma.tutorial;

import org.opendma.api.OdmaAssociation;
import org.opendma.api.OdmaContainable;
import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession16_PrintFolderAssociations
{

    public static void main(String[] args)
    {
        Lession16_PrintFolderAssociations lession = new Lession16_PrintFolderAssociations();
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
        for(OdmaAssociation assoc : folder.getAssociations())
        {
            System.out.print(indentStr.toString() + "    =" + assoc.getName());
            OdmaContainable containee = assoc.getContainable();
            System.out.println(" --> ID " + containee.getId() + " [" + containee.getOdmaClass().getQName() + "]");
        }
        for(OdmaFolder subFolder : folder.getSubFolders())
        {
            iterativePrintFolderContent(subFolder,indent+1);
        }
    }

}
