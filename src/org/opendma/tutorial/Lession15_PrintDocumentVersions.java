package org.opendma.tutorial;

import java.util.Iterator;

import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaSession;
import org.opendma.api.OdmaVersionCollection;

import com.xaldon.opendma.xmlrepo.XmlRepositorySessionProvider;

public class Lession15_PrintDocumentVersions
{

    public static void main(String[] args)
    {
        Lession15_PrintDocumentVersions lession = new Lession15_PrintDocumentVersions();
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
            OdmaId sampleDocumentId = new OdmaId("sample-document-a1");
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
        System.out.println("Versions of Document \"" + doc.getTitle() + "\":");
        OdmaVersionCollection versionCollection = doc.getVersionCollection();
        if(versionCollection != null)
        {
            OdmaDocument latest = versionCollection.getLatest();
            OdmaDocument released = versionCollection.getReleased();
            OdmaDocument inProgress = versionCollection.getInProgress();
            System.out.println("  Latest: "+(latest==null?"null":latest.getVersion()+" ID: "+latest.getId()));
            System.out.println("  Released: "+(released==null?"null":released.getVersion()+" ID: "+released.getId()));
            System.out.println("  InProgress: "+(inProgress==null?"null":inProgress.getVersion()+" ID: "+inProgress.getId()));
            System.out.println("  All Versions:");
            Iterable<OdmaDocument> allVersions = versionCollection.getVersions();
            if(allVersions != null)
            {
                Iterator<OdmaDocument> itAllVersions = allVersions.iterator();
                while(itAllVersions.hasNext())
                {
                    OdmaDocument ver = itAllVersions.next();
                    System.out.println("    "+ver.getVersion()+" ID: "+ver.getId());
                    System.out.println("        Title: "+ver.getTitle());
                }
            }
        }
        else
        {
            System.out.println("  Document does not support versioning.");
        }
    }

}
