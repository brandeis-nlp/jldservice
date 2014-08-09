package org.jldservice.server

/**
 * Created by lapps on 8/8/2014.
 */

import org.jldservice.server.WSDLClient;
import org.lappsgrid.api.Data

import javax.xml.namespace.QName;

class Lapps {
    //
    // "http://eldrad.cs-i.brandeis.edu:8282/opennlp-web-service/services/Splitter?wsdl"
    //
    public static String call(String wsdl, String text) {
        return call(wsdl, text, null, null);
    }

    public static String call(String wsdl, String text, String username, String password) {
        WSDLClient ws = new WSDLClient();
        ws.init(wsdl);
        ws.register(Data.class, new QName("uri:org.lappsgrid.api/", "Data"));
        //text = "Hi. How are you? This is Mike.";
        //String[] s = ws.callService("sentDetect", text);
        //System.out.println(s);
        if(username != null) {
            ws.authorize(username, password);
        }
        Data input = new Data();
        input.setPayload(text);
        if(text.trim().startsWith("{")) {
            input.setDiscriminator(1031);
        } else {
            input.setDiscriminator(3);
        }
        Data output = (Data) ws.callService("execute", input);
        return output.getPayload();
    }


}