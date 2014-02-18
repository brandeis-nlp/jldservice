package org.jldservice.json

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.json.Json
import edu.stanford.nlp.pipeline.Annotation
import junit.framework.Assert

class TestJson{

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }

    @Test
    public void testJson() {

//        def fil = "/Users/shi/Project/chunqishi/lapps/edu.brandeis.cs.opennlp-web-service/src/main/resources/en-sent.bin";
//        def ann = new Annotation("A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.")
//        def targetJs = "{\"keys\":[\"edu.stanford.nlp.ling.CoreAnnotations\$TextAnnotation\",null,null,null],\"values\":[\"A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.\",null,null,null],\"size\":1}";
//        def js = new Json();
//
//        Assert.assertEquals( targetJs,js.toJsonbyJackson(ann));
//        Assert.assertEquals(ann, js.fromJsonbyJackson(targetJs, Annotation.class))

//        def txtann = new edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation();
//        def targetTxtJs = "{}";
//        println js.toJsonbyGson(txtann);
//        Assert.assertEquals(targetTxtJs,js.toJsonbyGson(txtann));
//        Assert.assertEquals(txtann, js.fromJsonbyGson(targetTxtJs, edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation.class))

    }

}