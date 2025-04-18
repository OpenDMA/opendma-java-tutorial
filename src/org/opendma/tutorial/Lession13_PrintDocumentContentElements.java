package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaContentElement;
import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaSession;

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

        // register Adaptor
        Class.forName("com.xaldon.opendma.xmlrepo.Adaptor");

        // get Session
        OdmaSession session = AdaptorManager.getSession("xmlrepo:SampleRepository.xml", "tutorial", "tutorialpw");
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
        Iterable<OdmaContentElement> contentElements = doc.getContentElements();
        if(contentElements != null)
        {
            Iterator<OdmaContentElement> itContentElements = contentElements.iterator();
            while(itContentElements.hasNext())
            {
                OdmaContentElement contElem = itContentElements.next();
                System.out.println("  Pos " + contElem.getPosition() + " of type " + contElem.getContentType() + " as " + contElem.getOdmaClass().getQName() );
            }
        }
    }

}
