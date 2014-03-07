package org.jldservice.git

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Output
import com.esotericsoftware.kryo.io.Input
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.json.Json
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
        LGit.clone(https://github.com/chunqishi/lapps.git);
    }

}