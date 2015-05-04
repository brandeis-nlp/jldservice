package org.jldservice.clazz

import org.apache.commons.lang3.StringEscapeUtils
import org.jldservice.json.Json

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

class ClazzJson {
    static def call (obj, methodName, parameters, parameterTypes=[]) {
        def lastMethodName = methodName;
        if (methodName.contains(".")) {
            lastMethodName = methodName.split("\\.").last();
        }
        if (parameters.size() > 0) {
            return obj."$lastMethodName"(*parameters);
        } else {
            return obj."$lastMethodName"();
        }
    }

    static def invork(jsonobj, methodName, jsonParaObjs, jsonParaTypes=[]) {
        def obj = Json.fromJsonbyIOEx(jsonobj);
        def paraObjs = [];
        jsonParaObjs.eachWithIndex { CharSequence entry, int i ->
            def fixedEntry = entry;
            if (!entry.contains("@type")) {
                fixedEntry = "{\"@type\":\"" +jsonParaTypes.get(i) + "\",\"value\":\"" +
                        StringEscapeUtils.escapeJson(entry.replaceAll("^\\s*\"","").replaceAll("\"\\s*\$",""))  +"\"}";
            }
            def paraObj = Json.fromJsonbyIOEx(fixedEntry);
            paraObjs.add(paraObj);
        }
//        println "----invoke-----"
//        println paraObjs
//        println paraObjs.size()
        return call(obj, methodName, paraObjs)
    }


    static def init(clsName, jsonParaObjs, jsonParaTypes=[]) {
        def paraObjs = [];
        jsonParaObjs.eachWithIndex { CharSequence entry, int i ->
            def fixedEntry = entry;
            if (!entry.contains("@type")) {
                fixedEntry = "{\"@type\":\"" +jsonParaTypes.get(i) + "\",\"value\":\"" +
                        StringEscapeUtils.escapeJson(entry.replaceAll("^\\s*\"","").replaceAll("\"\\s*\$",""))  +"\"}";
            }
            def paraObj = Json.fromJsonbyIOEx(fixedEntry);
            paraObjs.add(paraObj);
        }
        if ( paraObjs.size() == 0 ) {
            return ClazzJar.load2obj(clsName);
        } else {
            return  ClazzJar.load(clsName).newInstance(*paraObjs);
        }
    }
}


