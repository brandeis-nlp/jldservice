package org.jldservice.maven
/**
 * @Chunqi SHI (diligence.cs@gmail.com)
 */


// http://groovy.codehaus.org/Updating+XML+with+XmlParser
// http://groovy.codehaus.org/api/groovy/util/NodeList.html

class PomXml{

    def xmlInput;
    def xmlOutput;
    def addDependencies(depxmlstr){
        if (xmlInput == null) {
            xmlInput = this.getClass().getResource("/pom.xml").text;
            xmlOutput = xmlInput;
        }
        xmlOutput = addDependencies(xmlOutput, depxmlstr)
        return xmlOutput
    }

    def setDependenciesCopyDirectory(directory){
        if (xmlInput == null) {
            xmlInput = this.getClass().getResource("/pom.xml").text;
            xmlOutput = xmlInput;
        }
        xmlOutput = setDependenciesCopyDirectory(xmlOutput, directory)
        return xmlOutput
    }

    static def addDependencies(pomxmlstr, depxmlstr){
        def pomxml = new XmlParser().parseText(pomxmlstr)
        def depxml = new XmlParser().parseText(depxmlstr)
        pomxml.dependencies.get(0).append(depxml)
        def writer = new StringWriter()
        new XmlNodePrinter(new PrintWriter(writer)).print(pomxml)
        def result = writer.toString()
        return result
    }

    static def setDependenciesCopyDirectory(pomxmlstr, directory) {
        def pomxml = new XmlParser().parseText(pomxmlstr)
        def plugin = pomxml.build.plugins.plugin.find{ it.artifactId.get(0).text() == "maven-dependency-plugin" }
        plugin.configuration.outputDirectory.get(0).setValue(directory)
        def writer = new StringWriter()
        new XmlNodePrinter(new PrintWriter(writer)).print(pomxml)
        def result = writer.toString()
        return result
    }

    static def AssemblyPlugin =       """
                                      <plugin>
                                        <artifactId>maven-assembly-plugin</artifactId>
                                        <version>2.4</version>
                                        <configuration>
                                          <descriptorRefs>
                                            <descriptorRef>jar-with-dependencies</descriptorRef>
                                          </descriptorRefs>
                                        </configuration>
                                      </plugin>
                                      """


    static def setAssemblyPlugin(pomxmlstr) {
        def pomxml = new XmlParser().parseText(pomxmlstr)
        def depxml = new XmlParser().parseText(AssemblyPlugin)
        pomxml.build.plugins.get(0).append(depxml)
        def writer = new StringWriter()
        new XmlNodePrinter(new PrintWriter(writer)).print(pomxml)
        def result = writer.toString()
        return result
    }

}
