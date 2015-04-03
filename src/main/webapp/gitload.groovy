import groovy.json.JsonBuilder
import org.codehaus.groovy.reflection.CachedMethod
import org.jldservice.clazz.ClazzInterface
import org.jldservice.clazz.ClazzJar
import org.jldservice.json.JsonSchema
import org.jldservice.maven.Maven
import org.jldservice.json.Json
import org.jldservice.clazz.Html


import org.jldservice.config.Config
import org.jldservice.git.LGit
import org.jldservice.maven.Maven
import groovy.servlet.GroovyServlet


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


Logger log = Logger.getLogger("gitload");
log.setLevel(Level.ALL);

//Handler handler = new FileHandler("display.log");
//handler.setFormatter(new SimpleFormatter());
//log.addHandler(handler);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
def static loadGitUrl(url){
    def localRepo =  LGit.git_clone(url);
    def pomDir = new File(localRepo).absolutePath;
    Maven.singleJar(pomDir);
    ClazzJar.getJarLoader(true);
}

def giturl = request.getParameter("giturl");
String ret = "";
if (giturl  != null) {
    ret = "URL: " + giturl;
    try{
        loadGitUrl(giturl.trim());
        ret = "SUCCEED: " + giturl;
    } catch (Throwable th) {
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        ret = "EXCEPTION: " + sw.toString();
    }
}


println """
<!DOCTYPE html>
<html>
<head>
    <title>JsonValid</title>

    <script src="http://code.jquery.com/jquery-1.10.2.js" >  </script>
    <!--script src="http://code.jquery.com/jquery-latest.min.js"></script-->
    <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js" >  </script>
    <script src="https://rawgithub.com/padolsey/prettyprint.js/master/prettyprint.js" >  </script>
    <script src="https://rawgithub.com/epeli/underscore.string/master/lib/underscore.string.js" >  </script>
    <script src="https://rawgithub.com/patricklodder/jquery-zclip/master/jquery.zclip.js" >  </script>


    <script src="http://warfares.github.io/pretty-json/libs/underscore-min.js" >  </script>
    <script src="http://warfares.github.io/pretty-json/libs/backbone-min.js" >  </script>
    <script src="http://warfares.github.io/pretty-json/pretty-json-min.js" >  </script>


    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/jsonld.js" >  </script>
    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/rdfa.js" >  </script>
    <script src="https://rawgithub.com/digitalbazaar/jsonld.js/master/js/request.js" >  </script>
    <script src="https://raw.githubusercontent.com/s3u/JSONPath/master/lib/jsonpath.js" >  </script>

</head>

<body>
    <h1  align="center">Git Loader</h1>

    <p> Please input the git url you want to load: <br/>
    <form method="get">
        <input name="giturl" type="text" size="100" />
        <input type="submit" value="load" />
    </form>

    <p>$ret
</body>
</html>

"""
