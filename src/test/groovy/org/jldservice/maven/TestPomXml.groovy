package org.jldservice.maven

import junit.framework.Assert

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
    public void testPomXml() {
        def xml = new PomXml()
        println "--------------addDependencies-----------------"
        def depadded = xml.addDependencies("""<dependency>
            <groupId>
                postgresql
            </groupId>
            <artifactId>
                postgresql
            </artifactId>
            <version>
                9.1-901.jdbc4
            </version>
        </dependency>""");

//        println depadded.trim()
//        println this.class.classLoader.getResource("pom_depaddedxml").text.trim()
//        def file = new File("/Users/shi/Project/chunqishi/jldservice/src/test/resources/pom_.xml");
//        file << depadded;

        Assert.assertTrue(depadded.contains("9.1-901.jdbc4"))

        println "---------setDependenciesCopyDirectory---------"
        depadded =  xml.setDependenciesCopyDirectory("mydirectory")

        Assert.assertTrue(depadded.contains("mydirectory"))
//        println ""
//        def mvn = new Maven()
//        mvn.copyDependencies(null,null)
    }

}