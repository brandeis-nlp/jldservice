import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.jldservice.clazz.ClazzInterface
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


/////////////////////////////////////////////////////////////////////////////////////////////////////////



def hrefClazz(clzname){
    return "<a href='display.groovy?clazzname="+ clzname +"' target='_blank'>" + clzname + "</a>"
}




def listInterface(paraclzname) {
    int METHOD_MODIFIERS = Modifier.PUBLIC  | Modifier.PROTECTED    | Modifier.PRIVATE |
            Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
            Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;

    def clsInf = new ClazzInterface()
    def idx = 0;
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
            invokebox.append("<b>${Field.getTypeName(method.getDeclaringClass())}</b> (");
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
                    <div class="green box"><span class="h">${Field.getTypeName(params[j])}</span><p><textarea cols="16" rows="3"></textarea></p></div>
                """);
                if (j < (params.length - 1)) {
                    htmllist.append(', ');
                    invokebox.append(',');
                }
            }
            htmllist.append(" )");
            invokebox.append(")");
            Class<?>[] exceptions = method.exceptionTypes; // avoid clone
            if (exceptions.length > 0) {
                htmllist.append(" throws ");
                for (int k = 0; k < exceptions.length; k++) {
                    htmllist.append(hrefClazz(exceptions[k].getName()));
                    if (k < (exceptions.length - 1))
                        htmllist.append(', ');
                }
            }
            htmllist.append("""
                <span onclick="togglePanel('invokePanel${idx}');return false;"> +</span>
                <br />
                <span id="invokePanel${idx}" class="invokePanel" style="display: none;">
                <form id="f${idx}">
                    ${invokebox.toString()}
                    <button class="button-red"
                        onclick="invokeMethod('f${idx}','res${idx}', 'JsonSplitter', 'sentPosDetect');return false;">invoke</button>

                    <button class="button-grey"
                        onclick="clearLog('res${idx}');return false;">clear</button>
                </form>
                <div id="res${idx}" class="code prettyprint">
                </div></span>
            """);
            htmllist.append("</li>");
            idx ++;
        }


        clsInf.pubFuncFromClassName(paraclzname).eachWithIndex{Object method, int i ->
            invokebox.setLength(0);
            htmllist.append("<li>");
            int mod = method.getCachedMethod().getModifiers() & METHOD_MODIFIERS;
            if (mod != 0) {
                htmllist.append(Modifier.toString(mod)).append(' ');
            }

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
            htmllist.append(" ");
            htmllist.append(Field.getTypeName(method.getCachedMethod().getDeclaringClass()));
            htmllist.append('.<b>');
            htmllist.append(method.getName());
            invokebox.append("""
                    <div class="blue box"><span class="h">this</span><p><textarea id="obj" cols="16" rows="3"></textarea></p></div>
            """);
            htmllist.append("</b> ( ");
            invokebox.append("""
                .<input type="text" value="${method.getName()}" disabled="disabled"/>(
            """);
            Class<?>[] params = method.getCachedMethod().parameterTypes;
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
                    <div class="green box"><span class="h">${Field.getTypeName(params[j])}</span><p><textarea cols="16" rows="3"></textarea></p></div>
                """);
                if (j < (params.length - 1)) {
                    htmllist.append(', ');
                    invokebox.append(',');
                }
            }

            htmllist.append(" ) ");
            invokebox.append(") ");

            Class<?>[] exceptions = method.getCachedMethod().exceptionTypes; // avoid clone
            if (exceptions.length > 0) {
                htmllist.append(" throws ");
                for (int k = 0; k < exceptions.length; k++) {
                    htmllist.append(hrefClazz(exceptions[k].getName()));
                    if (k < (exceptions.length - 1))
                        htmllist.append(', ');
                }
            }
            //    htmllist.append(method)
            htmllist.append("""
                <span onclick="togglePanel('invokePanel${idx}');return false;"> +</span>
                <br />
                <span id="invokePanel${idx}" class="invokePanel" style="display: none;">
                <form id="f${idx}">
                    ${invokebox.toString()}
                    <button class="button-red"
                        onclick="invokeMethod('f${idx}','res${idx}', 'ClassName', 'FunctionName');return false;">invoke</button>

                    <button class="button-grey"
                        onclick="clearLog('res${idx}');return false;">clear</button>
                </form>
                <div id="res${idx}" class="code prettyprint">
                </div></span>
            """);
            htmllist.append(" </li>");
            idx ++;
        }
        htmllist.append("</ul>");
    }
    return htmllist.toString()
}

def script = """
<script type="text/javascript">
<!--
function escapeHTML(str) {
  return str.replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}
function loadSample(id, sn, mn, sample){
  var span = \$("#" + id);
  if(span.html() == "loading...")
    span.html("<iframe src='" + document.URL + "/" + sn + "?method=" + mn + "&sample=" + sample + "'></iframe>");
}
var ppcfg = {maxArray: 1000, maxDepth: 100, maxStringLength: 1024,
  styles: {
    array: { th:{ backgroundColor: 'lightyellow', color: '#0', "text-align": "center" } },
    'function': { th: { backgroundColor: '#D82525' } },
    regexp: { th: { backgroundColor: '#E2F3FB', color: '#000' } },
    object: { th: { backgroundColor: '#A7C942', color: '#0', "text-align": "center" } },
    jquery : { th: { backgroundColor: '#FBF315' } },
    error: { th: { backgroundColor: 'red', color: 'yellow' } },
    domelement: { th: { backgroundColor: '#F3801E' } },
    date: { th: { backgroundColor: '#A725D8' } },
    colHeader: { th: { backgroundColor: '#FFFFFF', color: '#000', textTransform: 'uppercase', display: "none" } },
  } };

function invokeMethod(fn, text, sn, mn){
  var f = document.getElementById(fn);
  var n = f.elements.length;
  var io = {}
  io.Parameters = []
  for(i = 0; i < n; i++){
    var e = f.elements[i];
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
        io.Method = v;
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
      log = \$("<div></div>");
      t = \$("#" + text);
      if(t.children().length > 0){
        \$("<hr/>").insertBefore(t.children(":first"));
        log.insertBefore(t.children(":first"));
      } else{
        t.append(log);
      }
      log.append("<b>" + start.toLocaleString() + "</b> (" + (new Date().getTime() - start.getTime()) + " millisecond used)" +
        ' <span class="info">[:Raw Text]<span>Request:<br/>' + JSON.stringify(req) + "<br/><br/>Response:<br/>" + data +
        "</span></span><br/><b>Request:</b><br/>");
      log.append(\$(prettyPrint(io, ppcfg)));
      log.append("<b>Response:</b><br/>");
      log.append(\$(prettyPrint(jQuery.parseJSON(data), ppcfg)));
    },
    error: function(XMLHttpRequest, textStatus, errorThrown){
      var fb = "<font color=\\"red\\">";
      var fe = "</font>";
      \$("#" + text).append(
        start + ", " + (new Date().getTime() - start.getTime()) + "msec.<br/>　request: " + escapeHTML(req) +
        "<br/>status: " +
        fb + escapeHTML(textStatus) + fe + "<br/>error: " + fb + escapeHTML(errorThrown) + fe + "</font><hr/>"
        );
    }
    });
}

function clearLog(text){
  \$("#" + text).html("");
}

function togglePanel(id){
  \$("#" + id).toggle();
}
// -->
</script>
""";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//def ci = new ClazzInterface()

def paraclzname = request.getParameter("clazzname");
if (paraclzname == null || "".equals(paraclzname.trim())) {
    paraclzname = ClazzInterface.getName()
}

def parajsonldid = request.getParameter("jsonldid");
if (parajsonldid == null) {
    parajsonldid = ""
}

def parajsonobj = request.getParameter("jsonobj");
if (parajsonobj == null) {
    if (paraclzname != null) {
        parajsonobj = Json.toJsonPretty(this.class.classLoader.loadClass(paraclzname).newInstance());
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



def clz = this.class.classLoader.loadClass(paraclzname)
def htmlclz= JsonSchema.toJsonSchema(clz)

//def htmllist = Html.listInterface(paraclzname)
def htmllist = listInterface(paraclzname)

def head = this.class.getResource("/head.html").text

println """<!DOCTYPE HTML>
<html>
<meta charset="utf-8" />
<head>
    <!--TODO: title -->
    <title>Display Class</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
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
<p>
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
</p>

<a name="jsonld-description" />
<h2>JSON-LD Descirption  <a href="#_top_" style="text-decoration: none;">^</a></h2>
<hr/>
<h3>Input of Class:</h3>
<form method="get">
<p>
    Class Name: <br/> <input name="clazzname" formmethod="get" value="${paraclzname}" size="40" style="text-align:right"/>
</p>
<p>
    JSON-LD: <br/> <textarea name="jsonldid" formmethod="get" cols="40" rows="10">${parajsonldid}</textarea>
</p>

<p>
    JSON: <br/> <textarea name="jsonobj" formmethod="get" cols="40" rows="10">${parajsonobj}</textarea>
</p>

<p>
    <input type="reset" / ><input name="clazzname" type="submit"/>
</p>
</form>


<hr/>
<h3>JSON-Schema</h3>
<p>
<a href="http://json-schema.org/latest/json-schema-core.html" target="_blank">Json schma</a> of Class ${paraclzname}:
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
<p>
    Progress:
    <progress value="100" max="100"></progress></p>
<p>
    Contacts:
    <nonsense>diligenc</nonsense>s.cs@<nonsense>gma</nonsense>il.<nonsense></nonsense>com
</p></footer>
</body>
</html>
"""
