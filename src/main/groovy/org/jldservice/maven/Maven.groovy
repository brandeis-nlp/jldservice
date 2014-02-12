package org.jldservice.maven

import org.apache.maven.cli.MavenCli;
import java.io.File;

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

// https://github.com/mariuszs/maven-cli-example/blob/master/pom.xml
// https://github.com/mariuszs/maven-cli-example

class Maven {

    public static void copyDependencies (dependencies, libdirectory){
        def pomxml = new PomXml()
        pomxml.addDependencies(dependencies)
        def targetxml = pomxml.setDependenciesCopyDirectory(libdirectory)
        new File(libdirectory, "pom.xml").withWriter() { it << targetxml }
        MavenCli cli = new MavenCli();
        def cmds = ["dependency:copy-dependencies"] as String[];
        cli.doMain(cmds, libdirectory, System.out, System.out);
    }
}