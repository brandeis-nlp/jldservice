package org.jldservice.maven
/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.*



class TestPomXml {

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }


    @Test
    public void testMaven() {
        def xml = new PomXml()
        println "--------------addDependencies-----------------"
        println xml.addDependencies("<empty/>")
        println "---------setDependenciesCopyDirectory---------"
        println xml.setDependenciesCopyDirectory("mydirectory")
        println ""
        def mvn = new Maven()
//        mvn.copyDependencies(null,null)
    }

}