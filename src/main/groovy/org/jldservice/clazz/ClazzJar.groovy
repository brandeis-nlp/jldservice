package org.jldservice.clazz



import org.codehaus.groovy.reflection.CachedMethod
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

class ClazzJar {
    static String text = ClazzJar.class.getResource( '/jar.config' ).text
    static config = new ConfigSlurper().parse(text)

    static Object getConfig(String keyEx) {
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
            urls.add(jarFile.toURI().toURL());
        }
    }

    static ClassLoader jarLoader = null;


    static urls = new ArrayList<URL>();

    static def load(clazzName) {
        try{
            return ClazzJar.class.classLoader.loadClass(clazzName);
        } catch(java.lang.ClassNotFoundException e) {
            return getJarLoader().loadClass(clazzName);
        }
    }

    static ClassLoader getJarLoader () {
        if (jarLoader == null) {
            def jars = getConfig("path.jars").toString().split(File.pathSeparator);
            jars.eachWithIndex { Comparable<String> entry, int i ->
                loadJar(entry.toString().trim());
            }

            def libs = getConfig("path.libs").toString().split(File.pathSeparator);
            libs.eachWithIndex {entry, i ->
                File jardir = new File(entry.toString().trim());
                def jarpaths   = jardir.listFiles().findAll { it.name.endsWith('.jar') }
                jarpaths.eachWithIndex { Comparable<File> file, int j ->
                    loadJar(file.toString());
                }
            }
            jarLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        }
        return jarLoader;
    }
}


