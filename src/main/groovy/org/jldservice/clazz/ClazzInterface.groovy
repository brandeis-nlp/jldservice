package org.jldservice.clazz

import org.codehaus.groovy.reflection.CachedMethod

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

class NullClazz {
}


class ClazzInterface {

    def pubFuncFromClassName(clzname) {
        def clz = this.class.classLoader.loadClass(clzname)
        if (clz != null) {
            return pubFuncFromClass(clz)
        }
        return null
    }


    static def pubFuncNameDefaults() {
        def funcnames = new ArrayList()
        NullClazz.metaClass.methods.eachWithIndex { Object method, int i ->
            def key = method.name + ":" + method.parameterTypes
            funcnames.add(key.hashCode())
        }
        return funcnames
    }


    static def PubFuncDefaults = pubFuncNameDefaults()

    static def pubFuncFromClass(clz) {
        def funcs = []
        clz.metaClass.methods.each { method ->
            def key = method.name + ":" + method.parameterTypes
            if (!PubFuncDefaults.contains(key.hashCode()) ) {
                funcs.add(method)
            }
        }
        return funcs
    }

    def pubConstructorFromClassName(clzname) {
        def clz = this.class.classLoader.loadClass(clzname)
        if (clz != null) {
            return pubConstructorFromClass(clz)
        }
        return null
    }

    static def pubConstructorFromClass(clz) {
        def constructors = [];
        clz.getConstructors().eachWithIndex { AnnotatedElement entry, int i ->
            constructors.add(entry);
        }
        return constructors
    }
}