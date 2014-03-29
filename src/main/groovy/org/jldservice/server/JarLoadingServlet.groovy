package org.jldservice.server


import org.jldservice.config.Config
import org.jldservice.git.LGit
import org.jldservice.maven.Maven
import groovy.servlet.GroovyServlet

import javax.servlet.ServletConfig
import javax.servlet.ServletException

/**
 * Created by shi on 3/25/14.
 */


public class JarLoadingServlet extends GroovyServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        println "Hello, JarLoadingServlet."
//        loadJar();
    }


    Config config = new Config("/jld.config");
    public void loadJar(){
        String s = config.get("git.urls");
        String[] urls = s.split('\n');
        urls.eachWithIndex { Object entry, int i ->
            String url = entry.toString().trim();
            println url;
            def localRepo =  LGit.git_clone(url);
            def pomDir = new File(localRepo).absolutePath;
            Maven.singleJar(pomDir);
        }
    }
}