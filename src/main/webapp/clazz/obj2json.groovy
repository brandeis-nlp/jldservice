import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.codehaus.jettison.json.JSONObject
import org.jldservice.clazz.ClazzInterface
import org.jldservice.clazz.ClazzJar
import org.jldservice.json.JsonSchema
import org.jldservice.maven.Maven
import org.jldservice.json.Json
import org.jldservice.html.Html
import org.jldservice.cache.Cache
import groovy.xml.XmlUtil
import org.mortbay.util.ajax.JSON
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

Logger log = Logger.getLogger("obj2json");
log.setLevel(Level.ALL);

//Handler handler = new FileHandler("jsoninvoke.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);

def jsonIoObj, retJsonObj = [:], retJson;
def parameters = [];

def jsonIo = request.getParameter("io");
if (jsonIo == null || "".equals(jsonIo.trim())) {
    //
} else {
    jsonIoObj = JSON.parse(jsonIo);
}

log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
log.info(jsonIoObj.toString());

def paraclzname = jsonIoObj.ClazzName.toString();
retJson = Cache.get(jsonIo);
if ( retJson == null) {
    try{
        log.info("-----------------------------");
        log.info(paraclzname);
        def clz = null;
        def ins = null;
        if (paraclzname != null) {
            clz = ClazzJar.load(paraclzname);
            ins = clz.newInstance();
            if (jsonIoObj.Item == "Obj") {
                retJsonObj["Obj"] = Json.toJsonbyIO(ins);
            } else if (jsonIoObj.Item == "ObjEx") {
                retJsonObj["ObjEx"] = Json.toJsonbyIOEx(ins);
            } else {
                retJsonObj["Obj"] = Json.toJsonbyIO(ins);
                retJsonObj["ObjEx"] = Json.toJsonbyIOEx(ins);
            }
            retJsonObj["Except"]  = "Success.";
        }
    } catch (Throwable th) {
        log.info("=============================");
        def exp = th.toString() + ":";
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        String stackTrace = sw.toString();
        exp += stackTrace;
        log.info(exp);
        retJsonObj["Except"] = exp;
        retJsonObj["Obj"]  = "";
        retJsonObj["ObjEx"]  = "";
    }
    retJson = JSON.toString(retJsonObj);
    Cache.put(jsonIo, retJson);
} else {
    log.info("Using cache result.");
}
log.info("Json Length:" + retJson.length());
println """
${retJson}
"""