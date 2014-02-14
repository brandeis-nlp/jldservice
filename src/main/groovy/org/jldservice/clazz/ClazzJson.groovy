package org.jldservice.clazz



import org.codehaus.groovy.reflection.CachedMethod
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Modifier


import org.jldservice.json.Json

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

class ClazzJson {
    static def call (obj, methodName, parameters) {
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

    static def invork(jsonobj, methodName, jsonParaObjs) {
        def obj = Json.fromJsonbyIO(jsonobj);
        def paraObjs = [];
        jsonParaObjs.eachWithIndex { CharSequence entry, int i ->
            def paraObj = Json.fromJsonbyIO(entry);
            paraObjs.add(paraObj);
        }
        println "----invoke-----"
        println paraObjs
        println paraObjs.size()
        return call(obj, methodName, paraObjs)
    }


    static def init(clsName, jsonParaObjs) {
        def paraObjs = [];
        jsonParaObjs.eachWithIndex { CharSequence entry, int i ->
            def paraObj = Json.fromJsonbyIO(entry);
            paraObjs.add(paraObj);
        }
        return  ClazzJson.classLoader.loadClass(clsName).newInstance(*paraObjs);
    }
}


