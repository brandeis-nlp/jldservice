package org.jldservice.clazz

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.json.Json
import org.jldservice.clazz.ClazzJson
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.pipeline.Annotation
import junit.framework.Assert

class TestClazzJson{

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



        def c = ClazzJson.invork("{ \"@type\": \"java.lang.String\", \"value\": \"1234\" }", "charAt", ["{ \"@type\": \"int\", \"value\": \"0\" }"]);
        Assert.assertEquals(c , '1' as char);

        def fil = "/Users/shi/Project/chunqishi/lapps/edu.brandeis.cs.opennlp-web-service/src/main/resources/en-sent.bin";
        def docs = "A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.";
        def ann = new Annotation(docs);
        def targetJacksonJs = "{\"keys\":[\"edu.stanford.nlp.ling.CoreAnnotations\$TextAnnotation\",null,null,null],\"values\":[\"A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.\",null,null,null],\"size\":1}";
        def targetJs = "{\"@type\":\"edu.stanford.nlp.pipeline.Annotation\",\"keys\":[\"edu.stanford.nlp.ling.CoreAnnotations\$TextAnnotation\",null,null,null],\"values\":[\"A powerful storm that crippled much of the South swept into New York, depositing eight inches of snow in Central Park by midmorning and disrupting travel by road, rail and air.\",null,null,null],\"size\":1}";
        def js = new Json();
        Assert.assertEquals(targetJs,js.toJsonbyIO(ann));
        Assert.assertEquals(ann, js.fromJsonbyIO(targetJs));

        def senjson = js.toJsonbyIO(SentencesAnnotation.class);
        def senobj = js.fromJsonbyIO(senjson);


        def jsonobj =  TestClazzJson.getResource("/annotation.json").text;
        def para = "{\"@type\":\"class\",\"value\":\"edu.stanford.nlp.ling.CoreAnnotations\$SentencesAnnotation\"}";
        Assert.assertEquals(senjson, para);

        def clsName = "edu.stanford.nlp.pipeline.Annotation";
        def methodName = "edu.stanford.nlp.util.ArrayCoreMap.get";
        def jsonParaObjs = [para];
        def sentences = ClazzJson.invork(jsonobj, methodName, jsonParaObjs);

        def initAnn = ClazzJson.init(clsName, [js.toJsonbyIO(docs)]);
        def annotation = new Annotation(docs);
        Assert.assertEquals(annotation, initAnn);
        def props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        props.put("annotators", "tokenize, ssplit");
        def snlp = new StanfordCoreNLP(props);
        snlp.annotate(annotation);
        def annotationjson = js.toJsonbyIO(annotation);

//        def file = new File("/Users/shi/Project/chunqishi/jldservice/src/test/resources/annotation.json");
//        file << groovy.json.JsonOutput.prettyPrint(annotationjson);

        List<CoreMap> targetSentences = annotation.get(SentencesAnnotation.class);

        Assert.assertEquals(sentences.toString(), targetSentences.toString());
        println sentences;

    }

}