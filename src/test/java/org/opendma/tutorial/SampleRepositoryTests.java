package org.opendma.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.opendma.api.OdmaAssociation;
import org.opendma.api.OdmaChoiceValue;
import org.opendma.api.OdmaClass;
import org.opendma.api.OdmaContainable;
import org.opendma.api.OdmaDocument;
import org.opendma.api.OdmaFolder;
import org.opendma.api.OdmaId;
import org.opendma.api.OdmaPropertyInfo;
import org.opendma.api.OdmaRepository;
import org.opendma.api.OdmaSession;
import org.opendma.tck.OdmaTechnologyCompatibilityKit;

import com.xaldon.opendma.xmlrepo.Adaptor;

public class SampleRepositoryTests {
    
    @Test
    public void testConnect() throws Exception {
        Adaptor adaptor = new Adaptor();
        Properties connectionProps = new Properties();
        connectionProps.put("classpathResource", "SampleRepository.xml");
        OdmaSession session = adaptor.connect(connectionProps);
        session.close();
    }
    
    @Test
    public void testRepository() throws Exception {
        Adaptor adaptor = new Adaptor();
        Properties connectionProps = new Properties();
        connectionProps.put("classpathResource", "SampleRepository.xml");
        OdmaSession session = adaptor.connect(connectionProps);
        assertNotNull(session.getRepositoryIds());
        assertEquals(1,session.getRepositoryIds().size());
        OdmaId repoId = session.getRepositoryIds().get(0);
        assertNotNull(repoId);
        OdmaRepository repo = session.getRepository(repoId);
        assertNotNull(repo);
        List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaRepository(repo);
        assertTrue("Failed verifying repository: "+failures, failures.isEmpty());
        session.close();
    }
    
    @Test
    public void testClassHierarchy() throws Exception {
        Adaptor adaptor = new Adaptor();
        Properties connectionProps = new Properties();
        connectionProps.put("classpathResource", "SampleRepository.xml");
        OdmaSession session = adaptor.connect(connectionProps);
        OdmaId repoId = session.getRepositoryIds().get(0);
        OdmaRepository repo = session.getRepository(repoId);
        OdmaClass rootClass = repo.getRootClass();
        verifyClassTree(rootClass);
        session.close();
    }

    private void verifyClassTree(OdmaClass clazz) {
        List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaClass(clazz);
        assertTrue("Failed verifying clazz "+clazz.getId()+": "+failures, failures.isEmpty());
        for(OdmaPropertyInfo pi : clazz.getProperties()) {
            failures = OdmaTechnologyCompatibilityKit.verifyOdmaPropertyInfo(pi);
            assertTrue("Failed verifying property info "+pi.getId()+": "+failures, failures.isEmpty());
            for(OdmaChoiceValue cv : pi.getChoices()) {
                failures = OdmaTechnologyCompatibilityKit.verifyOdmaChoiceValue(cv);
                assertTrue("Failed verifying choice value "+cv.getId()+": "+failures, failures.isEmpty());
            }
        }
        for (OdmaClass child : clazz.getSubClasses()) {
            verifyClassTree(child);
        }
    }
    
    @Test
    public void testFolderHierarchy() throws Exception {
        Adaptor adaptor = new Adaptor();
        Properties connectionProps = new Properties();
        connectionProps.put("classpathResource", "SampleRepository.xml");
        OdmaSession session = adaptor.connect(connectionProps);
        OdmaId repoId = session.getRepositoryIds().get(0);
        OdmaRepository repo = session.getRepository(repoId);
        OdmaFolder rootFolder = repo.getRootFolder();
        verifyFolderTree(rootFolder);
        session.close();
    }

    private void verifyFolderTree(OdmaFolder folder) {
        List<String> failures = OdmaTechnologyCompatibilityKit.verifyOdmaFolder(folder);
        assertTrue("Failed verifying folder "+folder.getId()+": "+failures, failures.isEmpty());
        for (OdmaContainable cont : folder.getContainees()) {
            failures = OdmaTechnologyCompatibilityKit.verifyOdmaContainable(cont);
            assertTrue("Failed verifying containable "+cont.getId()+": "+failures, failures.isEmpty());
            if(cont instanceof OdmaDocument) {
                failures = OdmaTechnologyCompatibilityKit.verifyOdmaDocument(cont);
                assertTrue("Failed verifying document "+cont.getId()+": "+failures, failures.isEmpty());
            }
        }
        for (OdmaAssociation assoc : folder.getAssociations()) {
            failures = OdmaTechnologyCompatibilityKit.verifyOdmaAssociation(assoc);
            assertTrue("Failed verifying association "+assoc.getId()+": "+failures, failures.isEmpty());
        }
        for (OdmaFolder child : folder.getSubFolders()) {
            verifyFolderTree(child);
        }
    }

}
