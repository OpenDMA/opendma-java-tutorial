package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.OdmaSession;
import org.opendma.api.OdmaContentElement;
import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaQName;
import org.opendma.api.collections.OdmaContentElementEnumeration;

public class Lession13_PrintDocumentContentElements
{

    public static void main(String[] args)
    {
        Lession13_PrintDocumentContentElements lession1 = new Lession13_PrintDocumentContentElements();
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

            // get the SampleDocument by ID
            OdmaId repoId = new OdmaId("sample-repo");
            OdmaId sampleDocumentId = new OdmaId("sample-document-a2");
            OdmaDocument doc = (OdmaDocument)session.getObject(repoId, sampleDocumentId, new OdmaQName("tutorial","SampleDocument"), null);
            
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
        OdmaContentElementEnumeration contentElements = doc.getContentElements();
        if(contentElements != null)
        {
            Iterator<?> itContentElements = contentElements.iterator();
            while(itContentElements.hasNext())
            {
                OdmaContentElement contElem = (OdmaContentElement)itContentElements.next();
                System.out.println("  Pos " + contElem.getPosition() + " of type " + contElem.getContentType() + " as " + contElem.getOdmaClass().getQName() );
            }
        }
    }

}
