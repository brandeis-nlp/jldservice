//package org.jldservice.clazz
////
//import com.github.jsonldjava.core.JsonLdApi
//import com.github.jsonldjava.core.JsonLdOptions
//import com.github.jsonldjava.core.JsonLdProcessor
//import com.github.jsonldjava.utils.JSONUtils
//import groovy.json.JsonBuilder
////
/////**
//// * Created by Chunqi SHI (shicq@brandeis.edu) on 2/7/14.
//// */
////
//////
////// http://www.w3.org/TR/json-ld-api/#jsonldoptions
//////
//
//class JsonLd {
//
//    static def toJson(obj){
//        return new JsonBuilder(obj).toString()
//    }
//
//    // Define all JSON-LD keywords
//    static def all_jsonld_keywords = [
//            "@context", // L 0
//            "@id", // L 0
//            "@language", // L 1 @context
//            "@type", // L 0
//            "@container", // L1 @context
//            "@list", // L1 @context
//            "@set", // L 1 @context
//            "@reverse", // L 1 @context
//            "@index", // L 0
//            "@base", // L 1 @context
//            "@vocab", // L 1 @context
//            "@graph"  // L 0
//    ] as String []
//
//    // Define the JSON-LD keywords, which can be used in 0 level.
//    static def level0_jsonld_keywords = [
//            "@context", // L 0
//            "@id", // L 0
//            "@type", // L 0
//            "@index", // L 0
//            "@graph"  // L 0
//    ] as String []
//
//    static def ignoredProperties = [
//    ]
//
//    // Only level 0 keywords are considered.
//    static def configKeywords = []
//
//    static {
//        level0_jsonld_keywords.eachWithIndex { String entry, int i ->
//            configKeywords.add(entry.replace("@", "jsonld_"))
//        }
////        println configKeywords
//    }
//
//    def content
//    def jsonldobj
//
//    public JsonLd(content = null) {
//        this.content = content
//        Object[] jsonlds = null
//        def clsname = getClassLastName(content)
//        def root =  "class_" + clsname
//        def jsonconfigobj = [:]
//        configKeywords.eachWithIndex { String keywordEntry, int j ->
//            def configPath = root + "." + keywordEntry
//            def jsonconfig = getJsonObj(configPath)
//            if (jsonconfig != null)
//            // update jsonobj
//                jsonconfigobj.putAll(jsonconfig)
//        }
//
//        if (is_null(clsname)) {
//            jsonlds = null2jsonld(root, content)
//        } else if (is_str(clsname)) {
//            jsonlds = str2jsonld(root, content)
//        } else if (is_int(clsname)) {
//            jsonlds = int2jsonld(root, content)
//        } else if (is_float(clsname)) {
//            jsonlds = float2jsonld(root, content)
//        } else if (is_bool(clsname)) {
//            jsonlds = bool2jsonld(root, content)
//        } else if (is_map(clsname)) {
//            jsonlds = map2jsonld(root, content)
//        } else if (is_list(clsname)) {
//            jsonlds = list2jsonld(root, content)
//        } else {
//            jsonlds = obj2jsonld(root, content)
//        }
//        jsonldobj = null
//        if (jsonlds != null && jsonlds[0] != null) {
//            jsonldobj = jsonlds[0].clone()
////            def jsonldcontextobj = jsonconfigobj.get("@context", [:])
////            jsonldcontextobj.putAll(jsonlds[1])
//            jsonldobj.putAll(jsonconfigobj)
//        }
//    }
//
//    public Boolean isValid(){
//        return checkValid(jsonldobj)
//    }
//
//    public static Boolean checkValid(jsonldobj){
//        Map context = new HashMap();
//        JsonLdOptions options = new JsonLdOptions();
//        Object compact = JsonLdProcessor.compact(jsonldobj, context, options);
//        return true
//    }
//
//    public String toString(){
//        return new JsonBuilder(jsonldobj).toString()
//    }
//
//    public String toPrettyString(){
//        return new JsonBuilder(jsonldobj).toPrettyString()
//    }
//
//    def getClassLastName(obj){
//        if (obj == null)
//            return "null"
//        return obj.class.toString().split("\\.")[-1].toLowerCase()
//    }
//
//    def null2jsonld(root, str){
//        return [null, [:]] as Object[]
//    }
//
//    def bool2jsonld(root, str) {
//        def json = new JsonBuilder(str).toString()
//        def jsonobj = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@type":"xsd:boolean"]
//        return [jsonobj, jsonldcontextobj] as Object[]
//    }
//
//    def int2jsonld(root, str) {
//        def json = new JsonBuilder(str).toString()
//        def jsonobj = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@type":"xsd:integer"]
//        return [jsonobj, jsonldcontextobj] as Object[]
//    }
//
//    def float2jsonld(root, str) {
//        def json = new JsonBuilder(str).toString()
//        def jsonobj = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@type":"xsd:double"]
//        return [jsonobj, jsonldcontextobj] as Object[]
//    }
//
//    def str2jsonld(root, str) {
//        def json = new JsonBuilder(str).toString()
//        def jsonobj = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@type":"xsd:string"]
//        return [jsonobj, jsonldcontextobj] as Object[]
//    }
//
//    def list2jsonld(root, list) {
//        def json = new JsonBuilder(list).toString()
//        def jsonobj = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@container":"@list"]
//        return [jsonobj, jsonldcontextobj] as Object[]
//    }
//
//    def map2jsonld(root, Map map) {
//        def json = new JsonBuilder(map).toString()
//        def jsonld = JSONUtils.fromString(json)
//        def jsonldcontextobj = ["@container":"@index"]
//        map.eachWithIndex { Map.Entry<Object, Object> entry, int i ->
//            def propertyroot = root + "." + entry.key
//            def valclsname = getClassLastName(entry.value)
//            Object[] jsonlds = null
//            if (is_null(entry.value)) {
//                // TODO: what to do with null in map?
//                jsonlds = null2jsonld(propertyroot, entry.value)
//            } else if (is_bool(entry.value)) {
//                jsonlds = bool2jsonld(propertyroot, entry.value)
//            } else if (is_str(entry.value)) {
//                jsonlds = str2jsonld(propertyroot, entry.value)
//            } else if (is_int(entry.value)) {
//                jsonlds = int2jsonld(propertyroot, entry.value)
//            } else if (is_float(entry.value)) {
//                jsonlds = float2jsonld(propertyroot, entry.value)
//            } else if (is_map(entry.value)) {
//                jsonlds = map2jsonld(propertyroot, entry.value)
//            } else if (is_list(entry.value)) {
//                jsonlds = list2jsonld(propertyroot, entry.value)
//            } else {
//                jsonlds = obj2jsonld(propertyroot, entry.value)
//            }
//            if (jsonlds != null) {
//                jsonld.put(entry.key, jsonlds[0])
//                jsonldcontextobj.put(entry.key, jsonlds[1])
//            }
//        }
//        return [jsonld, jsonldcontextobj] as Object[]
//    }
//
//    /**
//     * from configure file reading the string using extend path.
//     * @param keyEx: extended key path.
//     * @return
//     */
//    def getJsonObj(String keyEx) {
//        String sval = get(keyEx)
//        if (sval == null)
//            return null
//        else
//            sval = sval.trim()
//        if (sval.charAt(0) != '{') {
//            sval = "{" + sval + "}" // only validated json can be parsed into JSON object.
//        }
//        return JSONUtils.fromString(sval)
//    }
//
//    def obj2jsonld(root, obj){
//        String json = new JsonBuilder(obj).toString()
//        def jsonld = JSONUtils.fromString(json)
//        def jsonldcontextobj = [:]
//
//        obj.getProperties().eachWithIndex { Map.Entry<Object, Object> entry, int i ->
//            if (!ignoredProperties.contains(entry.key)) {
//                def propertyroot = root + "." + entry.key
//                def valclsname = getClassLastName(entry.value)
//                Object[] jsonlds = null
//                if (is_null(entry.value)) {
//                    // TODO: what to do with null in map?
//                    jsonlds = null2jsonld(propertyroot, entry.value)
//                } else if (is_bool(entry.value)) {
//                    jsonlds = bool2jsonld(propertyroot, entry.value)
//                } else if (is_str(entry.value)) {
//                    jsonlds = str2jsonld(propertyroot, entry.value)
//                } else if (is_int(entry.value)) {
//                    jsonlds = int2jsonld(propertyroot, entry.value)
//                } else if (is_float(entry.value)) {
//                    jsonlds = float2jsonld(propertyroot, entry.value)
//                } else if (is_map(entry.value)) {
//                    jsonlds = map2jsonld(propertyroot, entry.value)
//                } else if (is_list(entry.value)) {
//                    jsonlds = list2jsonld(propertyroot, entry.value)
//                } else {
//                    jsonlds = obj2jsonld(propertyroot, entry.value)
//                }
//                if (jsonlds != null) {
//                    jsonld.put(entry.key, jsonlds[0])
//                    jsonldcontextobj.put(entry.key, jsonlds[1])
//                }
//            }
//        }
//        return [jsonld, jsonldcontextobj] as Object[]
//    }
//
//    def is_str(obj){
////        return "string" in clsname
//        return obj instanceof String
//    }
//
//    def is_map(obj) {
//        return obj instanceof Map
//    }
//
//    def is_list(obj) {
//        return obj instanceof List || obj instanceof Object[]
//    }
//
//    def is_null(obj) {
//        return obj == null
//    }
//
//    def is_bool(obj) {
//        return obj  instanceof Boolean
//    }
//
//    def is_int(obj) {
//        return obj instanceof Integer ||obj instanceof Long
//    }
//
//    def is_float(obj) {
//        return  obj instanceof Float ||obj instanceof Double
//    }
//
//    Object get(String keyEx) {
//        def target = config
//        keyEx.split("\\.").eachWithIndex {
//            key, i ->
//                if (target != null)
//                    target = target.get(key)
//        }
//        return target
//    }
//}
////
