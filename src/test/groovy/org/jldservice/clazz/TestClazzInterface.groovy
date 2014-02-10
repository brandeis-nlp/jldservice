package org.jldservice.clazz
/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.*

class TestMaven {

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "########################"
    }


    @Test
    public void test() {
        def ci = new ClazzInterface()
//        println ci.pubFuncFromClassName("org.jldservice.clazz.ClazzInterface")
        println ci.pubFuncFromClassName("org.apache.maven.cli.MavenCli")

    }

}