package org.opendma.tutorial;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.OdmaTypes;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaQName;

public class Lession03_GetRepository
{

    public static void main(String[] args)
    {
        Lession03_GetRepository lession = new Lession03_GetRepository();
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
            OdmaObject repo = session.getRepository(repoId);
            
            // get the opendma:DisplayName property by qualified name
            OdmaQName qnDisplayName = new OdmaQName("opendma", "DisplayName");
            OdmaProperty propDisplayName = repo.getProperty(qnDisplayName);
            
            // access by OdmaQName constant for well known property names
            repo.getProperty(OdmaTypes.PROPERTY_DISPLAYNAME);
            
            // print out the display name
            System.out.println("DispName: " + (String)propDisplayName.getValue() );
            System.out.println("DispName: " + propDisplayName.getString() );
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }

}
