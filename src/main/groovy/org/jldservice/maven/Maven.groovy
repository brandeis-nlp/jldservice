package org.jldservice.maven

import org.apache.maven.cli.MavenCli;
import java.io.File;

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

// https://github.com/mariuszs/maven-cli-example/blob/master/pom.xml
// https://github.com/mariuszs/maven-cli-example

class Maven {

    public void resolve (dependencies, targetclass){
        MavenCli cli = new MavenCli();
        System.out.println(new File(".").getAbsolutePath());
        def cmds = ["clean", "install"] as String[];
        cli.doMain(cmds, ".", System.out, System.out);
    }

}