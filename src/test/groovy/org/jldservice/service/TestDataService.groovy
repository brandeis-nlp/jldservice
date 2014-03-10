package org.jldservice.service


/**
 * Created by shicq on 3/4/14.
 */


import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.junit.Assert.*

class TestDataService {

    @Before
    public void setup() {
        println "~~~~~~~~~~~"+this.class.getName()+"~~~~~~~~~~~~~"
    }

    @After
    public void tearDown() {
        println "#################################################"
    }

    @Test
    public void testService() {
//        def serv = new DataService();
//        serv.service();
    }

}