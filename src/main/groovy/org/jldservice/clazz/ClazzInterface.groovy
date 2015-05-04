package org.jldservice.clazz

import java.lang.reflect.AnnotatedElement

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

class NullClazz {
}


class ClazzInterface {

    def pubFuncFromClassName(clzname) {
        def clz = ClazzJar.load(clzname);
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
        def clz = ClazzJar.load(clzname);
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