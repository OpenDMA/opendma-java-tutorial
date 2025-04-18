package org.opendma.tutorial;

import java.util.List;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaSession;

public class Lession02_ListVisibleRepositories
{

    public static void main(String[] args)
    {
        Lession02_ListVisibleRepositories lession = new Lession02_ListVisibleRepositories();
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
            // list all visible Repositories
            List<OdmaId> visibleRepositories = session.getRepositoryIds();
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
