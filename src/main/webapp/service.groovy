import org.jldservice.server.Lapps
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

Logger log = Logger.getLogger("json2json");
log.setLevel(Level.ALL);

//Handler handler = new FileHandler("service.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);


def jsonIoObj, retJsonObj = [:], retJson;
def parameters = [];

// read template, jsons from io
def jsonIo = request.getParameter("io");
if (jsonIo == null || "".equals(jsonIo.trim())) {

    // report NULL input.
} else {
    jsonIoObj = new JsonSlurper().parseText(jsonIo);
}

log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
log.info(jsonIoObj.toString());

// check cache.
retJson = Cache.get(jsonIo);
// not find.
if ( retJson == null) {
    try{
        log.info("-----------------------------");

        def wsdl = jsonIoObj.Wsdl;
        def input = jsonIoObj.Input;
        def output = Lapps.call(wsdl, input);
        log.info(output);
        retJsonObj["Output"] = output;
        retJsonObj["Except"] = "Sucess";
    } catch (Throwable th) {
        log.info("=============================");
        def exp = th.toString() + ":";
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        exp += stackTrace;
        log.info(exp);
        retJsonObj["Except"] = exp;
        retJsonObj["Output"] = "";
    }
    retJson = new JsonBuilder(retJsonObj).toString();
    Cache.put(jsonIo, retJson);
} else {
    log.info("Using cache result.");
}
log.info("Json Length:" + retJson.length());
println """
${retJson}
"""