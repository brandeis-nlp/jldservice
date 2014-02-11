package org.jldservice.server



import groovyx.net.ws.WSServer

//
// http://www.rolandfg.net/2014/01/06/web-apps-using-groovlets/
// https://github.com/mortenberg80/groovyJetty-quickstart
// http://www.kellyrob99.com/blog/2013/03/10/groovy-and-http-servers/
//

/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */


if (!session) {
    session = request.getSession(true);
}

if (!session.counter) {
    session.counter = 1
}

println """
<html>
    <head>
        <title>Groovy Servlet</title>
    </head>
    <body>
Hello, ${request.remoteHost}: ${session.counter}! ${new Date()}
    </body>
</html>
"""
session.counter = session.counter + 1