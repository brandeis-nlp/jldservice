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
//        xml.addDependencies("<empty/>")
    }

}