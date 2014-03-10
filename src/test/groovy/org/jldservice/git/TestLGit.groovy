package org.jldservice.git

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Output
import com.esotericsoftware.kryo.io.Input
import org.codehaus.plexus.util.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.maven.Maven
import edu.stanford.nlp.pipeline.Annotation
import junit.framework.Assert

class TestLGit{

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }


    @Test
    public void testClone() {
//        LGit.git_clone("https://github.com/chunqishi/lapps.git");
//        def localRepo =  LGit.git_url("https://github.com/chunqishi/lapps.git");
//        def pomDir = new File(localRepo, "edu.brandeis.cs.opennlp-web-service").absolutePath;
//        Maven.singleJar(pomDir);
//        Maven.merges(new File("target").absolutePath, new File(pomDir, "target").absolutePath);
//
//        pomDir = new File(localRepo, "edu.brandeis.cs.stanfordnlp-web-service").absolutePath;
//        Maven.singleJar(pomDir);
//        Maven.merges(new File("target").absolutePath, new File(pomDir, "target").absolutePath);
//
//        def jarFile = new File("target", "stanfordnlp-web-service-jar-with-dependencies.jar");
//        def classFile = new File("target", "classes");
//        LGit.unzip(jarFile.absolutePath, classFile.absolutePath);
    }

}