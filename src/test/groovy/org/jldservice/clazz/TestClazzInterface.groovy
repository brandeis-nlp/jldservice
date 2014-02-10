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
    }

    @After
    public void tearDown() {
    }


    @Test
    public void test() {
        def ci = new ClazzInterface()
        println ci.pubFuncFromClass(String.class)
    }

}