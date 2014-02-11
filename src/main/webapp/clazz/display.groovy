import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.jldservice.clazz.ClazzInterface
import org.jldservice.json.JsonSchema

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


static def toJson(obj){
    return new JsonBuilder(obj).toPrettyString()
}


def ci = new ClazzInterface()

def paraclzname = request.getParameter("clazzname");
if (paraclzname == null || "".equals(paraclzname.trim())) {
    paraclzname = ClazzInterface.getName()
}

def parajsonldid = request.getParameter("jsonldid");
if (parajsonldid == null) {
    parajsonldid = ""
}

if (!session) {
    session = request.getSession(true);
}

if (!session.counter) {
    session.counter = 1
}



def clz = this.class.classLoader.loadClass(paraclzname)
def htmlclz= JsonSchema.toJsonSchema(clz)


//
// http://groovy.codehaus.org/api/groovy/servlet/GroovyServlet.html
//


int METHOD_MODIFIERS = Modifier.PUBLIC  | Modifier.PROTECTED    | Modifier.PRIVATE |
                Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
                Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;

def hrefClazz(clzname){
    return "<a href='display.groovy?clazzname="+ clzname +"' target='_blank'>" + clzname + "</a>"
}

//
// org.codehaus.groovy.reflection.CachedMethod.toString()
//

StringBuilder htmllist = new StringBuilder();
if (paraclzname != null) {
htmllist.append("<ul>");
ci.pubFuncFromClassName(paraclzname).each{method ->
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
    htmllist.append("</b> ( ");
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
        if (j < (params.length - 1))
            htmllist.append(', ');
    }


    htmllist.append(" ) ");
    Class<?>[] exceptions = method.getCachedMethod().exceptionTypes; // avoid clone
    if (exceptions.length > 0) {
        htmllist.append(" throws ");
        for (int k = 0; k < exceptions.length; k++) {
            htmllist.append(hrefClazz(exceptions[k].getName()));
            if (k < (exceptions.length - 1))
                htmllist.append(',');
        }
    }
//    htmllist.append(method)
    htmllist.append("</li>");
}
htmllist.append("</ul>");
}

def head = this.class.getResource("/head.html").text

println """<!DOCTYPE HTML>
<html>
<meta charset="utf-8" />
<head>
    <!--TODO: title -->
    <title>Display Class</title>
    ${head}
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
