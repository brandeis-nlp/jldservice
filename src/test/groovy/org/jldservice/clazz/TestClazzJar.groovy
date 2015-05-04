package org.jldservice.clazz

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.json.Json
import org.jldservice.clazz.ClazzJar
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.pipeline.Annotation
import junit.framework.Assert

class TestClazzJar{

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }

    @Test
    public void test() {
        println ClazzJar.load("java.lang.String");

    }

}