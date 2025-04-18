package org.opendma.tutorial;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.opendma.AdaptorManager;
import org.opendma.api.OdmaCommonNames;
import org.opendma.api.OdmaContent;
import org.opendma.api.OdmaContentElement;
import org.opendma.api.OdmaDataContentElement;
import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaReferenceContentElement;
import org.opendma.api.OdmaSession;

public class Lession14_RetrieveDataFromContentElements
{

    public static void main(String[] args)
    {
        Lession14_RetrieveDataFromContentElements lession = new Lession14_RetrieveDataFromContentElements();
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
                if(contElem.instanceOf(OdmaCommonNames.CLASS_DATACONTENTELEMENT))
                {
                    System.out.println("    Data ContentElement");
                    System.out.println("    Size: "+((OdmaDataContentElement)contElem).getSize());
                    OdmaContent dataContent = ((OdmaDataContentElement)contElem).getContent();
                    if(dataContent != null)
                    {
                        System.out.print("    Conent:");
                        InputStream inDataContent = dataContent.getStream();
                        try
                        {
                            if(inDataContent != null)
                            {
                                int cnt = 0;
                                int data = 0;
                                while( (cnt++ < 10) && ((data=inDataContent.read())>0))
                                {
                                    System.out.print(" 0x");
                                    System.out.print(Integer.toHexString(data));
                                }
                                System.out.println("...");
                            }
                        }
                        catch(IOException ioe)
                        {
                            System.out.print("ERROR READING STREAM");
                        }
                        finally
                        {
                            try
                            {
                                inDataContent.close();
                            }
                            catch(IOException ioe)
                            {
                                System.out.print("ERROR CLOSING STREAM");
                            }
                        }
                    }
                    else
                    {
                        System.out.println("    Conent: NULL");
                    }
                }
                else if(contElem.instanceOf(OdmaCommonNames.CLASS_REFERENCECONTENTELEMENT))
                {
                    System.out.println("    Reference ContentElement");
                    System.out.println("    Location: "+((OdmaReferenceContentElement)contElem).getLocation());
                }
                else
                {
                    System.out.println("    Unknown sub-type of ContentElement.");
                    System.out.println("    Don't know how to retrieve the data.");
                }
            }
        }
    }

}
