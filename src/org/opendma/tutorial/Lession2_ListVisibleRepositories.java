package org.opendma.tutorial;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaId;

public class Lession2_ListVisibleRepositories
{

    public static void main(String[] args)
    {
        Lession2_ListVisibleRepositories lession1 = new Lession2_ListVisibleRepositories();
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
            // list all visible Repositories
            OdmaId[] visibleRepositories = session.getRepositoryIds();
            System.out.println("Visible Repositories:");
            for(OdmaId repoId : visibleRepositories)
            {
                System.out.println(repoId.toString());
            }
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

}
