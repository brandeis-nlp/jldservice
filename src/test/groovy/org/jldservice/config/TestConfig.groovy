package org.jldservice.config

import org.jldservice.config.Config
import org.junit.After
import org.junit.Before
import org.junit.Test

import junit.framework.Assert

class TestConfig{

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }


    @Test
    public void test() {
        def config = new Config("/jar.config");
        Assert.assertEquals(config.get("path.libs").trim(), "target") ;
        Assert.assertEquals(config.get("git.urls").trim(), "https://github.com/chunqishi/edu.brandeis.cs.opennlp-web-service.git\n" +
                "        https://github.com/chunqishi/edu.brandeis.cs.stanfordnlp-web-service.git") ;
    }

}