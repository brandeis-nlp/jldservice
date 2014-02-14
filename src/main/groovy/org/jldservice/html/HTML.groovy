package org.jldservice.html

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import org.codehaus.groovy.reflection.CachedMethod
import org.jldservice.clazz.ClazzInterface
import org.jldservice.json.JsonSchema
import org.jldservice.maven.Maven
import org.jldservice.json.Json
import groovy.xml.XmlUtil


class Html{


    static int METHOD_MODIFIERS = Modifier.PUBLIC  | Modifier.PROTECTED    | Modifier.PRIVATE |
            Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
            Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT;

    static def hrefClazz(clzname){
        return "<a href='display.groovy?clazzname="+ clzname +"' target='_blank'>" + clzname + "</a>"
    }




    static def listInterface(paraclzname) {
        def clsInf = new ClazzInterface()
        def idx = 0;
        //
        // org.codehaus.groovy.reflection.CachedMethod.toString()
        //
        StringBuilder htmllist = new StringBuilder();
        if (paraclzname != null) {
            htmllist.append("<ul>");
            clsInf.pubConstructorFromClassName(paraclzname).eachWithIndex {Object method, int i ->
                htmllist.append("<li>");
                int mod = method.getModifiers() & METHOD_MODIFIERS;
                if (mod != 0) {
                    htmllist.append(Modifier.toString(mod)).append(' ');
                }
                htmllist.append(" <b>");
                htmllist.append(Field.getTypeName(method.getDeclaringClass()));
                htmllist.append("</b> ( ");
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
                    if (j < (params.length - 1))
                        htmllist.append(', ');
                }
                htmllist.append(")");
                Class<?>[] exceptions = method.exceptionTypes; // avoid clone
                if (exceptions.length > 0) {
                    htmllist.append(" throws ");
                    for (int k = 0; k < exceptions.length; k++) {
                        htmllist.append(hrefClazz(exceptions[k].getName()));
                        if (k < (exceptions.length - 1))
                            htmllist.append(',');
                    }
                }
                htmllist.append("<span onclick=\"togglePanel('invokePanell${idx}');return false;\">+</span>")
                htmllist.append("<span id=\"invokePanel${idx}\" class=\"invokePanel\" style=\"display: none;\"><form id=\"f${idx}\"><textarea cols=\"4\" rows=\"1\"></textarea>[<a href=\"#\" onclick=\"invokeMethod('f${idx}','res${idx}', 'JsonSplitter', 'sentPosDetect');return false;\">invoke</a>][<a href=\"#\" onclick=\"clearLog('res${idx}');return false;\">clear</a>]</form><div id=\"res${idx}\" class=\"resPanel\"></div></span>");
                htmllist.append("</li>");
                idx ++;
            }


            clsInf.pubFuncFromClassName(paraclzname).eachWithIndex{Object method, int i ->
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
                htmllist.append("<span onclick=\"togglePanel('invokePanell${idx}');return false;\">+</span>")
                htmllist.append("<span id=\"invokePanel${idx}\" class=\"invokePanel\" style=\"display: none;\"><form id=\"f${idx}\"><textarea cols=\"4\" rows=\"1\"></textarea>[<a href=\"#\" onclick=\"invokeMethod('f${idx}','res${idx}', 'JsonSplitter', 'sentPosDetect');return false;\">invoke</a>][<a href=\"#\" onclick=\"clearLog('res${idx}');return false;\">clear</a>]</form><div id=\"res${idx}\" class=\"resPanel\"></div></span>");
                htmllist.append("</li>");
                idx ++;
            }
            htmllist.append("</ul>");
        }
        return htmllist.toString()
    }
}