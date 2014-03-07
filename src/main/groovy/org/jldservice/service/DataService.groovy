package org.jldservice.service


/**
 * Created by shicq on 3/4/14.
 */




@Grab('org.lappsgrid:client:1.0.3')
import org.lappsgrid.client.DataSourceClient

@Grab('org.lappsgrid:api:1.0.1')
import org.lappsgrid.api.Data


class DataService {

    def service () {
        def url = 'http://grid.anc.org:8080/service_manager/invoker/anc:masc.gold_1.4.0'
        def user = 'temporary'
        def pass = 'temporary'

        def client = new DataSourceClient(url, user, pass)
        String[] keys = client.list()
        def data = client.get(keys[0])
        println data.payload
    }
}