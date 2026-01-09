package org.opendma.tutorial;

import org.opendma.api.OdmaContentElement;
import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaSession;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession13_PrintDocumentContentElements
{

    public static void main(String[] args)
    {
        Lession13_PrintDocumentContentElements lession = new Lession13_PrintDocumentContentElements();
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

            // get the SampleDocument by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaId sampleDocumentId = new OdmaId("sample-document-a2");
            OdmaDocument doc = (OdmaDocument)session.getObject(repoId, sampleDocumentId, null);
            
            // print out properties of Sample Document
            printContentElements(doc);
            
        }
        finally
        {
            // always close the session
            session.close();
        }

    }
    
    public void printContentElements(OdmaDocument doc)
    {
        System.out.println("Content Elements of Document \"" + doc.getTitle() + "\":");
        for(OdmaContentElement contElem : doc.getContentElements())
        {
            System.out.println("  Pos " + contElem.getPosition() + " of type " + contElem.getContentType() + " as " + contElem.getOdmaClass().getQName() );
        }
    }

}
