package org.jldservice.json

import com.cedarsoftware.util.io.JsonReader
import com.cedarsoftware.util.io.JsonWriter
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.google.gson.Gson
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import org.codehaus.jackson.map.ObjectMapper
import groovy.json.JsonOutput

import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger
import org.jldservice.clazz.ClazzJar

class Json {
    static Logger log = Logger.getLogger("display");
    static {
        log.setLevel(Level.ALL);
    }



    //
    //  https://code.google.com/p/google-gson/
    //
    static def toJsonbyGson(obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }

    static def fromJsonbyGson(json, cls) {
        Gson gson = new Gson();
        def obj = gson.fromJson(json, cls);
        return obj
    }

    static def toJsonbyJackson(obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        def json = mapper.writeValueAsString(obj);
        return json;
    }

    static def fromJsonbyJackson(json, cls) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        def obj = mapper.readValue(json, cls);
        return obj;
    }

    static Kryo kryo = new Kryo();
    static Output output = new Output(1024*1024*256);

    static def isSerializable(obj){
        try{
            output.clear();
            kryo.writeObject(output, obj);
            byte[] bytes = output.getBuffer();
            output.close();
            Input input = new Input(bytes);
            kryo.readObject(input, obj.class);
            input.close();
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    static def sizeOfSerializable(obj){
        try{
            output.clear();
            kryo.writeObject(output, obj);
            byte[] bytes = output.getBuffer();
            output.close();
            Input input = new Input(bytes);
            kryo.readObject(input, obj.class);
            input.close();
            log.info("Size:" + output.total())
            return output.total();
        }catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    //
    // https://code.google.com/p/json-io/
    //
    static def toJsonbyIO(obj) {
        if (obj instanceof String) {
            return "{\"@type\":\"java.lang.String\",\"value\": \"${obj}\"}"
        } else {
            return JsonWriter.objectToJson(obj);
        }
    }

    static def sizeLimit = 1024;

    static def toJsonbyIOEx(obj) {
        if(sizeOfSerializable(obj) < sizeLimit) {
            return toJsonbyIO(obj);
        }
        return "{\"@type\":\"" + obj.class.getName() + "\",\"@except\": \"${obj}\"}"
    }

    static def slurper = new JsonSlurper()

    static def fromJsonbyIOEx(json) {
        if(json.contains("\"@except\":")) {
            def clsName = slurper.parseText(json).get("@type").trim();
            return ClazzJar.load(clsName).newInstance();
        }
        return fromJsonbyIO(json)
    }

    static def fromJsonbyIO(json) {
        try{
            return JsonReader.jsonToJava(json)
        }catch(Exception e) {
            def clsName = slurper.parseText(json).get("@type").trim();
            return ClazzJar.load(clsName).newInstance();
        }
    }


    // default using Json-IO
    static def toJsonPretty(obj){
        def json = toJsonbyIO(obj);
        log.info(json);
        return JsonOutput.prettyPrint(json);
    }

}