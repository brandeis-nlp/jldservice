import org.jldservice.server.Lapps
import org.jldservice.server.Lapps2
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.jldservice.cache.Cache

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

Logger log = Logger.getLogger("service.groovy");
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
            def output = Lapps.call(jsonObjIn.Wsdl, jsonObjIn.Input, jsonObjIn.Username, jsonObjIn.Password);
            log.info("Return:" + output);
            jsonObjRet["Output"] = output;
            jsonObjRet["Except"] = "Sucess";
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