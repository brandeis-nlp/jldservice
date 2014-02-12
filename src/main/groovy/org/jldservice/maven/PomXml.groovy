package org.jldservice.maven

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import groovy.util.XmlParser

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


}
//
//def input = '''
//<shopping>
//    <category type="groceries">
//        <item>Chocolate</item>
//        <item>Coffee</item>
//    </category>
//    <category type="supplies">
//        <item>Paper</item>
//        <item quantity="4">Pens</item>
//    </category>
//    <category type="present">
//        <item when="Aug 10">Kathryn's Birthday</item>
//    </category>
//</shopping>
//'''
//
//def expectedResult = '''
//<shopping>
//  <category type="groceries">
//    <item>Luxury Chocolate</item>
//    <item>Luxury Coffee</item>
//  </category>
//  <category type="supplies">
//    <item>Paper</item>
//    <item quantity="6" when="Urgent">Pens</item>
//  </category>
//  <category type="present">
//    <item>Mum's Birthday</item>
//    <item when="Oct 15">Monica's Birthday</item>
//  </category>
//</shopping>
//'''
//
//def root = new XmlParser().parseText(input)
//
//// modify groceries: quality items please
//def groceries = root.category.findAll{ it.@type == 'groceries' }.item[0]
//groceries.each { g ->
//    g.value = 'Luxury ' + g.text()
//}
//
//// modify supplies: we need extra pens
//def supplies = root.category.findAll{ it.@type == 'supplies' }.item[0]
//supplies.findAll{ it.text() == 'Pens' }.each { s ->
//    s.@quantity = s.@quantity.toInteger() + 2
//    s.@when = 'Urgent'
//}
//
//// modify presents: August has come and gone
//def presentCategory = root.category.find{ it.@type == 'present' }
//presentCategory.children().clear()
//presentCategory.appendNode('item', "Mum's Birthday")
//presentCategory.appendNode('item', [when:'Oct 15'], "Monica's Birthday")
//
//// check the when attributes
//def removeNulls(list) { list.grep{it} }
//assert removeNulls(root.'**'.item.@when) == ["Urgent", "Oct 15"]
//
//// check the whole document using XmlUnit
//def writer = new StringWriter()
//new XmlNodePrinter(new PrintWriter(writer)).print(root)
//def result = writer.toString()
//
//XMLUnit.setIgnoreWhitespace(true)
//def xmlDiff = new Diff(result, expectedResult)
//assert xmlDiff.identical()