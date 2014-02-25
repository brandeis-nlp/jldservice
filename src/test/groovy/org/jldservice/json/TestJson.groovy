package org.jldservice.json

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Output
import com.esotericsoftware.kryo.io.Input
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
        def js = new Json();
        def json = null;
        def obj = null;
        edu.brandeis.cs.lappsgrid.opennlp.POSTagger tag = new edu.brandeis.cs.lappsgrid.opennlp.POSTagger();
        edu.brandeis.cs.lappsgrid.opennlp.OpenNLP op = new edu.brandeis.cs.lappsgrid.opennlp.OpenNLP();
//        Kryo kryo = new Kryo();
//        Output output = new Output(1024*1024*256);
//        kryo.writeObject(output, tag);
//        byte[] bytes = output.getBuffer();
//        output.close();
//        Input input = new Input(bytes);
//        obj = kryo.readObject(input, edu.brandeis.cs.lappsgrid.opennlp.POSTagger.class);
//        input.close();
//        json = js.toJsonbyGson(tag);
//        obj = js.fromJsonbyGson(fromJsonbyJackson, edu.brandeis.cs.lappsgrid.opennlp.POSTagger.class);
        Assert.assertEquals(false, js.isSerializable(tag));
        Assert.assertEquals(true, js.isSerializable(op));



//        def fil = "/Users/shi/Project/chunqishi/lapps/edu.brandeis.cs.opennlp-web-service/src/main/resources/en-sent.bin";
//        def ann = new Annotation("A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.")
//        def targetJs = "{\"keys\":[\"edu.stanford.nlp.ling.CoreAnnotations\$TextAnnotation\",null,null,null],\"values\":[\"A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.\",null,null,null],\"size\":1}";

//
//        Assert.assertEquals( targetJs,js.toJsonbyJackson(ann));
//        Assert.assertEquals(ann, js.fromJsonbyJackson(targetJs, Annotation.class))

//        def txtann = new edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation();
//        def targetTxtJs = "{}";
//        println js.toJsonbyGson(txtann);
//        Assert.assertEquals(targetTxtJs,js.toJsonbyGson(txtann));
//        Assert.assertEquals(txtann, js.fromJsonbyGson(targetTxtJs, edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation.class))

//        println js.toJsonbyJackson("Hi, How are you?");
//        println js.fromJsonbyJackson("{Hi, How are you?}",java.lang.String.class);

    }

}