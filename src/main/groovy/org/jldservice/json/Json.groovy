package org.jldservice.json

import com.cedarsoftware.util.io.JsonReader
import com.cedarsoftware.util.io.JsonWriter
import com.google.gson.Gson
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import org.codehaus.jackson.map.ObjectMapper
import groovy.json.JsonOutput


class Json {
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

    static def fromJsonbyIO(json) {
        return JsonReader.jsonToJava(json)
    }

    // default using Json-IO
    static def toJsonPretty(obj){
        return JsonOutput.prettyPrint(toJsonbyIO(obj));
    }

}