package org.jldservice.json

import groovy.json.JsonBuilder
import org.ini4j.Wini

/**
 * Created by lapps on 5/2/2015.
 */
class BratJson {


//    entity_types: [ {
//        type   : 'Person',
//        /* The labels are used when displaying the annotion, in this case
//        we also provide a short-hand "Per" for cases where
//        abbreviations are preferable */
//
//        labels : ['Person', 'Per'],
//        // Blue is a nice colour for a person?
//
//        bgColor: '#7fa2ff',
//        // Use a slightly darker version of the bgColor for the border
//
//        borderColor: 'darken'
//    } ]


    static def readLine(String line) {
        def arr =  line.split("\\s|,").findAll{line.length() > 0};
        def subMap = [:];
        for (def s : arr[1..arr.size()-1].sort{it}) {
            def pair = s.split("\\:")
            subMap.put(pair[0], pair[1]);
        }
        return [(arr[0]):subMap]
    }

    static def readConf(String s){
        def map = new LinkedHashMap();
        def list = new ArrayList();
        for(def line:s.split("\n")) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("#")) {
                if(line.startsWith("[")) {
                    list = new ArrayList();
                    map.put(line.substring(1, line.length() - 1), list)
                } else {
                    list.add(line);
                }
            }
        }
        return map;
    }

    static def embedJson(File annConfFile, File visualConfFile) {
        return embedJson(annConfFile.text, visualConfFile.text);
    }

    static def embedJson(String annConf, String visualConf) {
        def annIni = readConf(annConf);
        def visIni = readConf(visualConf);
        def labelMap = [:], drawMap = [:];
        visIni.get("labels").each {
            def arr = it.split(" \\| ");
            labelMap.put(arr[0], arr[1..arr.size()-1].sort{it});
        }

        println visIni.get("drawing")
        visIni.get("drawing").each {
            def arr =  it.split("\\s|,").findAll{it.length() > 0};
            def subMap = [:];
            for (def s : arr[1..arr.size()-1].sort{it}) {
                def pair = s.split("\\:")
                subMap.put(pair[0], pair[1]);
            }
            drawMap.put(arr[0], subMap);
//            println arr[0]
//            println subMap
        }
        visIni.get("options")
//        annIni.
        JsonBuilder builder = new JsonBuilder();
        builder {
            entity_types (
                annIni.get('entities').collect{
                    [type:it, labels:labelMap.get(it, [:])] + ((Map)drawMap.get("SPAN_DEFAULT",[:]) + (Map)drawMap.get(it, [:]))
                }
            )

            relations_types ({
                annIni.get('relations').collect{
                    [type:it, labels:labelMap.get(it, [:])] + ((Map)drawMap.get("ARC_DEFAULT",[:]) + (Map)drawMap.get(it, [:]))
                }
            })

            events_types ({
                annIni.get('events').collect{
                    [type:it, labels:labelMap.get(it, [:])] + ((Map)drawMap.get("ARC_DEFAULT",[:])  + (Map)drawMap.get(it, [:]))
                }
            })

            entity_attribute_types ({
                annIni.get('attributes').collect{
                    [type:it]
                }
            })
        }
        return builder.toPrettyString();
    }

    static final def main(String [] args) {
        println embedJson(this.class.getResource("/brat/annotation.conf").text,
                this.class.getResource("/brat/visual.conf").text)
    }
}
