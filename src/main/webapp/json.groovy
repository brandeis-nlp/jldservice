import org.lappsgrid.json2json.Json2Json
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.codehaus.groovy.reflection.CachedMethod
import groovy.xml.XmlUtil
import org.jldservice.cache.Cache
import groovy.util.logging.Log

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

//Handler handler = new FileHandler("json2json.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);

def json2json = new Json2Json();


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

        def template = jsonIoObj.Template;
        def json = jsonIoObj.Json;
        def trans = json2json.transform(template, json);
        log.info(trans);
        retJsonObj["Transform"] = trans;
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
        retJsonObj["Transform"] = "";
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