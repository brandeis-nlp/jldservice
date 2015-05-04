import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.jldservice.cache.Cache
import org.lappsgrid.wsdlclient.WSDLClient

// application
import javax.servlet.ServletContext
// request
import javax.servlet.http.HttpServletRequest
// response
import javax.servlet.http.HttpServletResponse
// session
import javax.servlet.http.HttpSession
// out
import java.io.PrintWriter
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

Logger log = Logger.getLogger("json2json.groovy");
log.setLevel(Level.ALL);

//Handler handler = new FileHandler("service.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);


def txtIn, jsonObjIn, jsonObjRet = [:], txtRet;

// read io parameter from request
txtIn = request.getParameter("io");
log.info("~~~~~~~~~~~~~BEGIN~~~~~~~~~~~~~~~~");
log.info("txtIn: " + txtIn);

// if only it  is json text format
if (txtIn != null && txtIn.trim().startsWith('{')) {
    // check cache
    txtRet = Cache.get(txtIn);
    if(txtRet != null)
        log.info("Cached: " + txtRet);
    else {
        // text to json object
        jsonObjIn = new JsonSlurper().parseText(txtIn);
        try{
            log.info("-----------Call------------------");
            WSDLClient ws = new WSDLClient();
            ws.init(jsonObjIn.Wsdl);
            if(jsonObjIn.Username != null) {
                ws.authorize(jsonObjIn.Username, jsonObjIn,Password);
            }
            String output = ws.callService("",jsonObjIn.Op, *jsonObjIn.Input).toString();
            jsonObjRet['Output'] = output;
            log.info("Return: Output.length()=" +output.length());
            jsonObjRet["Except"] = "Success";
        } catch (Throwable th) {
            log.info("============Error=================");
            StringWriter sw = new StringWriter();
            th.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            def exp = th.toString() + " : " + stackTrace;
            log.info("Error:" + exp);
            jsonObjRet["Except"] = exp;
            jsonObjRet["Output"] = "";
        }
        // json object to text
        txtRet = new JsonBuilder(jsonObjRet).toString();
        // put into chache
        Cache.put(txtIn, txtRet);
    }
}
log.info("txtRet.length() : " + txtRet.length());

// response by print text
println """
    ${txtRet}
"""
log.info(".............End...................");