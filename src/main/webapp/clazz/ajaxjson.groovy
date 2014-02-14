import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.codehaus.jettison.json.JSONObject
import org.jldservice.clazz.ClazzInterface
import org.jldservice.clazz.ClazzJson
import org.jldservice.json.JsonSchema
import org.jldservice.maven.Maven
import org.jldservice.json.Json
import org.jldservice.html.Html
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

//def jsonObj, methodName, jsonParaObjs=[], retJson;
//
//def paraObj = request.getParameter("obj");
//if (paraObj == null || "".equals(paraObj.trim())) {
//    //
//} else {
//    jsonObj = paraObj;
//}
//def paraParas = request.getParameter("paras");
//if (paraParas == null || "".equals(paraParas.trim())) {
//    //
//} else {
//    JSONObject jsonArr = JSON.parse(paraParas);
//    jsonArr.eachWithIndex { JSONObject entry, int i ->
//        jsonParaObjs.add(JSON.toString(entry));
//    }
//}
//
//def paraMethod = request.getParameter("method");
//if (paraMethod == null || "".equals(paraMethod.trim())) {
//    //
//}else {
//    methodName = paraMethod;
//}
//


Logger log = Logger.getLogger("ajaxjson");
log.setLevel(Level.ALL);
Handler handler = new FileHandler("ajaxjson.log");
handler.setFormatter(new SimpleFormatter());
log.addHandler(handler);

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

try{
    if (jsonIoObj != null){
        log.info(jsonIoObj.Parameters.toString());
        //ClazzJson.invork("{ \"@type\": \"java.lang.String\", \"value\": \"1234\" }", "charAt", ["{ \"@type\": \"int\", \"value\": \"0\" }"]);
        log.info("-----------------------------");
        retJsonObj = ClazzJson.invork(jsonIoObj.Object as String, jsonIoObj.Method as String, jsonIoObj.Parameters as ArrayList);
        log.info("result:"+retJsonObj.toString());


        retJson = Json.toJsonbyIO(retJsonObj);
        log.info("result:"+retJson.toString());
    }
} catch (Exception e) {
    log.info("=============================");
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    String stackTrace = sw.toString();
    log.info(e.toString());
    retJsonObj = ["Exception":e.toString(), "StackTrace":stackTrace];
    retJson = JSON.toString(retJsonObj);
}

println """
${retJson}
"""