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

class ClazzJar {
    static String text = ClazzJar.class.getResource( '/ClazzJar.config' ).text
    static config = new ConfigSlurper().parse(text)

    static Object get(String keyEx) {
        def target = config
        keyEx.split("\\.").eachWithIndex {
            key, i ->
                if (target != null)
                    target = target.get(key)
        }
        return target
    }


    static void loadJar(jar) {
        File jarFile = new File(jar);
        if (jarFile.exists()){
            ClazzJar.class.classLoader.rootLoader.addURL(jarFile.toURI().toURL());
        }
    }

    static void load () {
        def jars = get("path.jars").toString().split(File.pathSeparator);
        jars.eachWithIndex { Comparable<String> entry, int i ->
            loadJar(entry);
        }
        def libs = get("path.libs").toString().split(File.pathSeparator);
        libs.eachWithIndex { Comparable<String> entry, int i ->
            File jardir = new File(entry);
            def jarpaths   = jardir.listFiles().findAll { it.name.endsWith('.jar') }
            jarpaths.eachWithIndex { Comparable<File> file, int j ->
                loadJar(file.toString());
            }
        }
    }
}


