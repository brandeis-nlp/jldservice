package org.jldservice.clazz


import org.jldservice.cache.Cache
import org.jldservice.config.Config

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

    static void addJar2Urls(jar) {
        File jarFile = new File(jar);
        if (jarFile.exists()){
            urls.add(jarFile.toURI().toURL());
        }
    }

    static ClassLoader jarLoader = null;


    static urls = new ArrayList<URL>();


    static def load2obj(clazzName) {
        def obj = Cache.get(clazzName);
        if ( obj == null) {
            obj = load(clazzName).newInstance();
            Cache.put(clazzName, obj);
        }
        return obj;
    }

    static def load(clazzName) {
        try{
            return ClazzJar.class.classLoader.loadClass(clazzName);
        } catch(Throwable e) {
            return getJarLoader().loadClass(clazzName);
        }
    }

    static void loadJarDir(File dir) {
        dir.eachFile { fil ->
            if (fil.name.toLowerCase().endsWith('.jar')) {
                addJar2Urls(fil.getAbsolutePath());
            }
        }
        dir.eachDir { subDir ->
            loadJarDir(subDir)
        }
    }

    static ClassLoader getJarLoader (reload=false) {
        if (jarLoader == null || reload) {
            def jars = Config.getDef("path.jars").toString().split(File.pathSeparator);
            jars.eachWithIndex { Comparable<String> entry, int i ->
                addJar2Urls(entry.toString().trim());
            }

            def libs = Config.getDef("path.libs").toString().split(File.pathSeparator);
            libs.eachWithIndex {entry, i ->
                loadJarDir(new File(entry.toString().trim()))
            }
//            libs.eachWithIndex {entry, i ->
//                File jardir = new File(entry.toString().trim());
//                def jarpaths   = jardir.listFiles().findAll { it.name.endsWith('.jar') }
//                jarpaths.eachWithIndex { Comparable<File> file, int j ->
//                    loadJar(file.toString());
//                }
//            }
//            jarLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

            //http://stackoverflow.com/questions/252893/how-do-you-change-the-classpath-within-java


            // Add the conf dir to the classpath
            // Chain the current thread classloader
            println "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
            println urls
            println "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"

            ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
            jarLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), currentThreadClassLoader);
//            jarLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), this.classLoader);
            // Replace the thread classloader - assumes
            // you have permissions to do so
            Thread.currentThread().setContextClassLoader(jarLoader);
        }
        return jarLoader;
    }
}


