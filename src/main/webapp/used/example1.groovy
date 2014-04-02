import org.jldservice.clazz.ClazzInterface
def ci = new ClazzInterface()


if (!session) {
    session = request.getSession(true);
}

if (!session.counter) {
    session.counter = 1
}

def htmllist = "<ul>"
ci.pubFuncFromClassName("org.apache.maven.cli.MavenCli").each{method ->
   htmllist +="<li>" + method + "</li>"
}
htmllist +="</ul>"

println """
<html><head>
<title>Groovlets 101</title>
</head>
<body>
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

<p>
${htmllist}
</p>
</body>
</html>
"""
