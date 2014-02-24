import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.jldservice.clazz.ClazzInterface
import org.jldservice.clazz.ClazzJar
import org.jldservice.json.JsonSchema
import org.jldservice.maven.Maven
import org.jldservice.json.Json
import org.jldservice.html.Html
import groovy.xml.XmlUtil

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

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.logging.Level
import java.util.logging.Logger


/////////////////////////////////////////////////////////////////////////////////////////////////////////


Logger log = Logger.getLogger("display");
log.setLevel(Level.ALL);

//Handler handler = new FileHandler("ajaxjson.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);

def hrefClazz(clzname){
    return "<a href='display.groovy?clazzname="+ clzname +"' target='_blank'>" + clzname + "</a>"
}


def lastNameOf(paraclzname) {
    if (paraclzname == null)
        return "null";
    return paraclzname.split("\\.")[-1];
}


def listInterface(paraclzname, jsonobj="", log=null) {
    int METHOD_MODIFIERS = Modifier.PUBLIC  | Modifier.PROTECTED    | Modifier.PRIVATE |
            Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
            Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;

    def clsInf = new ClazzInterface()
    def idx = 0;

    if (log != null)
        log.info("Begin...");
    //
    // org.codehaus.groovy.reflection.CachedMethod.toString()
    //
    StringBuilder htmllist = new StringBuilder();
    StringBuilder invokebox = new StringBuilder();
    if (paraclzname != null) {
        htmllist.append("<ul>");
        clsInf.pubConstructorFromClassName(paraclzname).eachWithIndex {Object method, int i ->
            invokebox.setLength(0);
            htmllist.append("<li>");
            int mod = method.getModifiers() & METHOD_MODIFIERS;
            if (mod != 0) {
                htmllist.append(Modifier.toString(mod)).append(' ');
            }
            htmllist.append(" <b>");
            htmllist.append(Field.getTypeName(method.getDeclaringClass()));
            htmllist.append("</b> ( ");
            invokebox.append("""
                <input type="text" value="${Field.getTypeName(method.getDeclaringClass())}" size="${Field.getTypeName(method.getDeclaringClass()).length()* 1.2 + 2}" disabled="disabled"/>
            """);
            invokebox.append("(");
            Class<?>[] params = method.parameterTypes;
            for (int j = 0; j < params.length; j++) {
                if (params[j].isPrimitive()) {
                    htmllist.append(Field.getTypeName(params[j]));
                } else if (params[j].isArray()) {
                    if (params[j].getComponentType().isPrimitive()) {
                        htmllist.append(Field.getTypeName(params[j].getComponentType()));
                    } else{
                        htmllist.append(hrefClazz(Field.getTypeName(params[j].getComponentType())));
                    }
                    htmllist.append("[]");
                } else {
                    htmllist.append(hrefClazz(Field.getTypeName(params[j])));
                }
                invokebox.append("""
                    <div class="green box"><span class="h">${Field.getTypeName(params[j])}</span><p>
                    <input id="ptype" type="text" style="display:none" value="${Field.getTypeName(params[j])}" />
                    <textarea cols="32" rows="6"></textarea></p></div>
                """);
                if (j < (params.length - 1)) {
                    htmllist.append(', ');
                    invokebox.append(',');
                }
            }
            htmllist.append(" )");
            invokebox.append(")");
//            Class<?>[] exceptions = method.exceptionTypes; // avoid clone
//            if (exceptions.length > 0) {
//                htmllist.append(" throws ");
//                for (int k = 0; k < exceptions.length; k++) {
//                    htmllist.append(hrefClazz(exceptions[k].getName()));
//                    if (k < (exceptions.length - 1))
//                        htmllist.append(', ');
//                }
//            }
            htmllist.append("""
                <span onclick="\$('#div${idx}').toggle();return false;"> +</span>
                <br />
                <div id="div${idx}" style="display: none;">
                <form id="form${idx}">
                    ${invokebox.toString()}
                    <button class="button-red"
                        onclick="invoke('form${idx}','display${idx}');return false;">Invoke</button>

                    <button class="button-grey"
                        onclick="\$('#display${idx}').html('');return false;">Clear</button>
                </form>
                <div id="display${idx}" class="block"></div>
                </div>
            """);
            htmllist.append("</li>");
            idx ++;
        }

//        if (log != null)
//            log.info("Methods...");


        clsInf.pubFuncFromClassName(paraclzname).eachWithIndex{Object method, int i ->
//            if (log != null)
//                log.info("Method:" + method);
            invokebox.setLength(0);
            htmllist.append("<li>");
            int mod = method.getCachedMethod().getModifiers() & METHOD_MODIFIERS;
            if (mod != 0) {
                htmllist.append(Modifier.toString(mod)).append(' ');
            }

//            if (log != null)
//                log.info("Return type...");

            if (method.getReturnType().isPrimitive()) {
                htmllist.append(Field.getTypeName(method.getReturnType()));
            } else if (method.getReturnType().isArray()) {
                if (method.getReturnType().getComponentType().isPrimitive()) {
                    htmllist.append((Field.getTypeName(method.getReturnType().getComponentType()))).append("[]");
                } else{
                    htmllist.append(hrefClazz(Field.getTypeName(method.getReturnType().getComponentType()))).append("[]");
                }
            } else {
                htmllist.append(hrefClazz(Field.getTypeName(method.getReturnType())));
            }

//            if (log != null)
//                log.info("Method name...");

            htmllist.append(" ");
            htmllist.append(Field.getTypeName(method.getCachedMethod().getDeclaringClass()));
            htmllist.append('.<b>');
            htmllist.append(method.getName());

//            if (log != null)
//                log.info("Box");

            invokebox.append("""<div class="blue box"><span class="h">""");
            invokebox.append(Field.getTypeName(method.getCachedMethod().getDeclaringClass()));
            invokebox.append("""
                </span><p><textarea id="obj" cols="32" rows="6"
                    onclick="if(this.value == '') {this.value=document.getElementById('jsonobj').value;}; return false;"></textarea></p></div>
                    """);

//            if (log != null)
//                log.info("Input");

            htmllist.append("</b> ( ");
            invokebox.append("""
                .<input type="text" value="${method.getName()}" size="${method.getName().length() * 1.2 + 2}" disabled="disabled"/>(
            """);

//            if (log != null)
//                log.info("Parameters...");


            Class<?>[] params = method.getCachedMethod().parameterTypes;
//            if (log != null)
//                log.info("Parameters:" + params);


            for (int j = 0; j < params.length; j++) {
                if (params[j].isPrimitive()) {
                    htmllist.append(Field.getTypeName(params[j]));
                } else if (params[j].isArray()) {
                    if (params[j].getComponentType().isPrimitive()) {
                        htmllist.append(Field.getTypeName(params[j].getComponentType()));
                    } else{
                        htmllist.append(hrefClazz(Field.getTypeName(params[j].getComponentType())));
                    }
                    htmllist.append("[]");
                } else {
                    htmllist.append(hrefClazz(Field.getTypeName(params[j])));
                }
                invokebox.append("""
                    <div class="green box"><span class="h">${Field.getTypeName(params[j])}</span><p>
                    <input id="ptype" type="text" style="display:none" value="${Field.getTypeName(params[j])}" />
                    <textarea cols="32" rows="6"></textarea></p></div>

                """);
                if (j < (params.length - 1)) {
                    htmllist.append(', ');
                    invokebox.append(',');
                }
            }

            htmllist.append(" ) ");
            invokebox.append(") ");

//            Class<?>[] exceptions = method.getCachedMethod().exceptionTypes; // avoid clone
//            if (exceptions.length > 0) {
//                htmllist.append(" throws ");
//                for (int k = 0; k < exceptions.length; k++) {
//                    htmllist.append(hrefClazz(exceptions[k].getName()));
//                    if (k < (exceptions.length - 1))
//                        htmllist.append(', ');
//                }
//            }
            //    htmllist.append(method)
            htmllist.append("""
                <span onclick="\$('#div${idx}').toggle();return false;"> +</span>
                <br />
                <div id="div${idx}" style="display: none;">
                <form id="form${idx}">
                    ${invokebox.toString()}
                    <button class="button-red"
                        onclick="invoke('form${idx}','display${idx}');return false;">Invoke</button>

                    <button class="button-grey"
                        onclick="\$('#display${idx}').html('');return false;">Clear</button>
                </form>
                <div id="display${idx}" class="block"></div>
                </div>
            """);
            htmllist.append(" </li>");
            idx ++;
        }
        htmllist.append("</ul>");
    }
//    if (log != null)
//        log.info("End.");

    return htmllist.toString()
}

def script = """
<script type="text/javascript">
<!--
function invoke(formId, displayId){
  var form = document.getElementById(formId);
  var io = {}
  io.Parameters = []
  io.ParameterTypes = []
  for(i = 0; i < form.elements.length; i++){
    var e = form.elements[i];
    var v = e.value;
    var jsonobj = v;
    if (v[0] == '{' || v[0] == '[') {
        jsonobj = JSON.parse(v);
    }
    if (e.nodeName == 'TEXTAREA') {
        if (e.id == 'obj') {
            io.Object = v;
        } else {
            io.Parameters.push(v);
        }
    } else if (e.nodeName == 'INPUT') {
        if (e.id == 'ptype') {
            io.ParameterTypes.push(v);
        } else {
            io.Method = v;
        }
    }
  }
  var req = {};
  req.io = JSON.stringify(io);
  var start = new Date();
  \$.ajax({
    type: "POST",
    dataType: "text",
    url: "ajaxjson.groovy",
    data: req,
    success: function(data, dataType) {
      var display = \$("<div></div>");
      t = \$("#" + displayId);
      if(t.children().length > 0){
        \$("<hr/>").insertBefore(t.children(":first"));
        display.insertBefore(t.children(":first"));
      } else{
        t.append(display);
      }
      display.append("<p>");
      display.append("<b>" + start.toLocaleString() + "</b> (" + (new Date().getTime() - start.getTime()) + " millisecond used) ");
      display.append("<span class='dropt'>...<span style='width:500px;'>Request:<br />" + JSON.stringify(io,null,4) + "</span></span></p>");
      display.append("<hr />");
      display.append("<p>");
      display.append("<div class='red box'><span class='h'>Result</span><p><textarea cols='32' rows='6'>" + data + "</textarea></p></div>");
      display.append("</p><p>")
      display.append("<div style='inline-block;'><span id='response" + start.getTime() + "'> </span></div>");
      var node = new PrettyJSON.view.Node({
                el:\$('#response' + start.getTime()),
                data:jQuery.parseJSON(data)
      });
      display.append("</p><p>");
      display.append(\$(prettyPrint(jQuery.parseJSON(data))));
      display.append("</p>")
    },
    error: function(XMLHttpRequest, textStatus, errorThrown){
      var display = \$("<div></div>");
      t = \$("#" + displayId);
      if(t.children().length > 0){
        \$("<hr/>").insertBefore(t.children(":first"));
        display.insertBefore(t.children(":first"));
      } else{
        t.append(display);
      }
      display.append("<p>");
      display.append("<b>" + start.toLocaleString() + "</b> (" + (new Date().getTime() - start.getTime()) + " millisecond used)");
      display.append("<span class='info'> [ ... ] <span>" + JSON.stringify(io,null,4) + "</span></span></p>");
      display.append("</p>")
      display.append("<hr />");
      display.append("<p>Status:<font color='red'>")
      display.append(_.unescape(textStatus))
      display.append("</font></p>")

      display.append("<p>Error:<font color='red'>")
      display.append(_.unescape(errorThrown))
      display.append("</font></p>")

    }
  });
}

\$(document).ready(function(){
  var jsonString = document.getElementById("jsonobj").value;
  var jsonPretty = JSON.stringify(JSON.parse(jsonString),null,2);
  document.getElementById("jsonobj").value = jsonPretty;


});

// -->
</script>
""";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//def ci = new ClazzInterface()

def paraclzname = request.getParameter("clazzname");
if (paraclzname == null || "".equals(paraclzname.trim())) {
    paraclzname = ClazzInterface.getName()
} else {
    paraclzname = paraclzname.trim();
}

def parajsonldid = request.getParameter("jsonldid");
if (parajsonldid == null) {
    parajsonldid = ""
}

//def parajsonobj = request.getParameter("jsonobj");
def parajsonobj = null;
def parajsonexp = null;
def clz = null;
def ins = null;
if (parajsonobj == null || "".equals(parajsonobj.trim())) {
    if (paraclzname != null) {
        try{
            clz = ClazzJar.load(paraclzname);
            ins = clz.newInstance();
//            log.info("Initialized: " + ins)
            parajsonobj = Json.toJsonbyIO(ins);
//            log.info("Json ready: " + ins)
            parajsonexp = "Success.";
        } catch(Throwable th) {
            parajsonexp = th.toString() + ":";
            StringWriter sw = new StringWriter();
            th.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            parajsonexp += stackTrace;
            parajsonobj = "";
        }
    }
}

//def paramavendep = request.getParameter("mavendep");
//def libpath = application.getRealPath("WEB-INF/lib")
//if (paramavendep == null || "".equals(paramavendep.trim())) {
//    paramavendep = "";
//} else {
//    if (!new File(libpath).exists()) {
//        new File(libpath).mkdirs()
//    }
//    new Maven().copyDependencies(paramavendep, libpath);
//}

if (!session) {
    session = request.getSession(true);
}

if (!session.counter) {
    session.counter = 1
}

def htmlclz= JsonSchema.toJsonSchema(clz);
//log.info("JsonSchema ready: " + htmlclz);

//def htmllist = Html.listInterface(paraclzname)
def htmllist = listInterface(paraclzname, parajsonobj, log);
//log.info("listInterface: " + htmllist);

def head = this.class.getResource("/head.html").text;
log.info("Output page ...");

println """<!DOCTYPE HTML>
<html>
<meta charset="utf-8" />
<head>
    <!--TODO: title -->
    <title>Display Class</title>
    ${head}
    ${script}
</head>
<body>
    <a name="_top_" />
    <nav class="nav-bar">
        <a href="#jsonld-description">jsonld descirption</a>
        |
        <a href="#class-interface">class interfaces</a></nav>


<p>
</p>
<!--p>
${request}
</p>
<p>
${application}
</p>
<p>
Hello, ${request.remoteHost}: ${session.counter}! ${new Date()}
</p>
<p>
Welcome to Groovlets 101. As you can see
this Groovlet is fairly simple.
</p>
<p>
This course is being run on the following servlet container: </br>
${application.getServerInfo()}
</p-->

<a name="jsonld-description" />
<h2>JSON-LD Description  <a href="#_top_" style="text-decoration: none;"></a></h2>
<hr/>
<h3>Input of Class:</h3>
<form method="get">
<p>
    Class Name: <br/>
    <input name="clazzname" formmethod="get" value="${paraclzname}" size="${paraclzname.length() * 1.5}"/>
    <!--textarea name="clazzname" formmethod="get" cols="40" rows="1" style="text-align:right"> ${paraclzname} &nbsp;</textarea-->
    <input type="submit" value="Submit"/>
    <input type="reset" value="Reset"/>
</p>

<!--p>
    JSON-LD: <br/> <textarea name="jsonldid" formmethod="get" cols="80" rows="10">${parajsonldid}</textarea>
</p-->

<p>
    JSON: <br/> <textarea id="jsonobj" readonly name="jsonobj" formmethod="get" cols="80" rows="10">${parajsonobj}</textarea>
    <span class='dropt'>.<span style='width:600px; font-size:11px;'>Exception:<br />${parajsonexp}</span></span><br/>
</p>

</form>


<hr/>
<h3>JSON-Schema</h3>
<p>
<a href="http://json-schema.org/latest/json-schema-core.html" target="_blank">Json schema</a> of Class ${paraclzname}:
<pre class="prettyprint linenums">
${htmlclz}
</pre>
</p>

<a name="class-interface" />
<h2>Class Interface  <a href="#_top_" style="text-decoration: none;">^</a></h2>
<p>
Original Functions of Class ${paraclzname}:
</p>

<p>
${htmllist}
</p>


<footer><hr/>
<!--p>
    Progress:
    <progress value="100" max="100"></progress></p>
<p>
    Contacts:
    <nonsense>diligenc</nonsense>s.cs@<nonsense>gma</nonsense>il.<nonsense></nonsense>com
</p--></footer>
</body>
</html>
"""
