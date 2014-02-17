package org.jldservice.json

import org.junit.After
import org.junit.Before
import org.junit.Test

import org.jldservice.json.JsonSchema


class TestJsonSchema {

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }

    @Test
    public void testMaven() {
        def js = new JsonSchema()
        println js.toJsonSchema(String.class)
        println js.toJsonSchema(java.io.UnsupportedEncodingException.class)
    }

}