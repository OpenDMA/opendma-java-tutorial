package org.opendma.tutorial;

import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaObject;
import org.opendma.api.OdmaProperty;
import org.opendma.api.OdmaQName;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

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

        // get Session
        XmlRepositorySessionProvider sessionProvider = new XmlRepositorySessionProvider();
        sessionProvider.setClasspathResource("SampleRepository.xml");
        OdmaSession session = sessionProvider.getSession();

        try
        {

            // get the repository by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaObject repo = session.getRepository(repoId);
            
            // get the opendma:DisplayName property by qualified name
            OdmaQName qnDisplayName = new OdmaQName("opendma", "DisplayName");
            OdmaProperty propDisplayName = repo.getProperty(qnDisplayName);
            
            // access by OdmaQName constant for well known property names
            repo.getProperty(OdmaCommonNames.PROPERTY_DISPLAYNAME);
            
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
