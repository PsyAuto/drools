package org.drools.compiler.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.drools.compiler.kproject.AbstractKnowledgeTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.StatelessKieSession;

@RunWith(CDITestRunner.class)
public class StatelessKieSessionInjectionTest {
    public static AbstractKnowledgeTest helper;
    
    @Inject
    @KSession("jar1.KSession1")  
    @KReleaseId( groupId    = "jar1",
                 artifactId = "art1", 
                 version    = "1.0" )
    private StatelessKieSession kbase1ksession1v10;
    
    @Inject
    @KSession("jar1.KSession1")  
    @KReleaseId( groupId    = "jar1",
                 artifactId = "art1", 
                 version    = "1.1" )
    private StatelessKieSession kbase1ksession1v11;   
    
    @Inject   
    @KSession(value="jar1.KSession1", name="sks1")
    @KReleaseId( groupId    = "jar1",
                 artifactId = "art1", 
                 version    = "1.0" )
    private StatelessKieSession kbase1ksession1sks1;
    
    @Inject
    @KSession(value="jar1.KSession1", name="sks2")    
    @KReleaseId( groupId    = "jar1",
                 artifactId = "art1", 
                 version    = "1.0" )
    private StatelessKieSession kbase1ksession1sks2  ;  
    
    @Inject
    @KSession(value="jar1.KSession1", name="sks2")    
    @KReleaseId( groupId    = "jar1",
                 artifactId = "art1", 
                 version    = "1.0" )      
    private StatelessKieSession kbase1ksession1sks22;      
    
    @BeforeClass
    public static void beforeClass() {  
        helper = new AbstractKnowledgeTest();
        try {
            helper.setUp();
        } catch ( Exception e ) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
        try {
            helper.createKieModule( "jar1", true, "1.0" );
            helper.createKieModule( "jar1", true, "1.1" );
        } catch ( Exception e ) {
            fail( "Unable to build dynamic KieModules:\n" + e.toString() );
        }

        CDITestRunner.setUp( );

        CDITestRunner.weld = CDITestRunner.createWeld( StatelessKieSessionInjectionTest.class.getName() );

        CDITestRunner.container = CDITestRunner.weld.initialize();
    }

    @AfterClass
    public static void afterClass() {
        CDITestRunner.tearDown();
        
        try {
            helper.tearDown();
        } catch ( Exception e ) {
            fail( e.getMessage() );
        }
    }     
    
    @Test
    public void testDynamicStatelessKieSessionReleaseId() throws IOException, ClassNotFoundException, InterruptedException {
        assertNotNull( kbase1ksession1v10 );
        assertNotNull( kbase1ksession1v10 );
        
        KieCommands cmds  = KieServices.Factory.get().getCommands();
                
        List<String> list = new ArrayList<String>();
        kbase1ksession1v10.setGlobal( "list", list );
        kbase1ksession1v10.execute( cmds.newFireAllRules() );
        
        assertEquals( 2, list.size() );
        assertTrue( list.get(0).endsWith( "1.0" ) );
        assertTrue( list.get(1).endsWith( "1.0" ) );
        
        list = new ArrayList<String>();
        kbase1ksession1v11.setGlobal( "list", list );
        kbase1ksession1v11.execute( cmds.newFireAllRules() );
        
        assertEquals( 2, list.size() );
        assertTrue( list.get(0).endsWith( "1.1" ) );
        assertTrue( list.get(1).endsWith( "1.1" ) );        
    }    
    
    @Test
    public void testNamedStatelessKieSessions() throws IOException, ClassNotFoundException, InterruptedException {
        assertNotNull(kbase1ksession1sks1);
        assertNotNull(kbase1ksession1sks2);
        assertNotNull(kbase1ksession1sks22);
        
        assertNotSame(kbase1ksession1sks1, kbase1ksession1sks2);
        assertSame( kbase1ksession1sks2, kbase1ksession1sks22);     
        
        KieCommands cmds  = KieServices.Factory.get().getCommands();
        
        List<String> list = new ArrayList<String>();
        kbase1ksession1sks1.setGlobal( "list", list );
        kbase1ksession1sks1.execute( cmds.newFireAllRules() );
        
        assertEquals( 2, list.size() );
        assertTrue( list.get(0).endsWith( "1.0" ) );
        assertTrue( list.get(1).endsWith( "1.0" ) );
        
        list = new ArrayList<String>();
        kbase1ksession1sks2.setGlobal( "list", list );
        kbase1ksession1sks2.execute( cmds.newFireAllRules() );
        
        assertEquals( 2, list.size() );
        assertTrue( list.get(0).endsWith( "1.0" ) );
        assertTrue( list.get(1).endsWith( "1.0" ) );          
    }   
          
}
