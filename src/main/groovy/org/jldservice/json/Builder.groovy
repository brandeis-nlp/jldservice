package org.jldservice.json


bratdsl = """{
     def idx = 0
     def lastview = &\$payload.views[-1]
     def lastviewanns = lastview==null?null:lastview.annotations
     def lastviewfeatures = lastviewanns==null?null:lastviewanns.features
     def parse = lastviewfeatures == null? null:lastviewfeatures.select{&.penntree!=null}.penntree
     def coref = lastviewanns.select{&."@type" != null && &."@type".contains("Coreference")}.features.mentions
     def markables = lastviewanns.select{&."@type" != null && &."@type".contains("Markable") && coref.toString().contains(&.id)}

     text &\$payload.text."@value" +  (parse == null||parse.size == 0||parse[0]==null?"":"\n~~~~\n"+parse[0])

     relations (lastviewanns.select{&."@type" != null && &."@type".contains("DependencyStructure")}.features.dependencies
                .flatten().foreach{
                ["D\${idx++}", &.label, [["Governor", &.features.governor], ["Dependent", &.features.dependent]]]
                })

     equivs (lastviewanns.select{&."@type" != null && &."@type".contains("Coreference")}.features
                .flatten().foreach{
                                 ["*", "Coreference", &.mentions[0], &.mentions[1]]
                                 })

     entities (lastviewanns.unique{&.start+" "+&.end}.select{&.features != null && (&.features.category != null || &.features.pos != null)}.foreach{
       [&.id == null?"T\${idx++}":&.id, &.features.category != null?&.features.category.trim().toUpperCase():&.features.pos.trim().toUpperCase(), [[&.start.toInteger(), &.end.toInteger()]]]
     } + markables.foreach{
       [&.id == null?"M\${idx++}":&.id, "Mention", [[&.start.toInteger(), &.end.toInteger()]]]
     })
}
"""

lappsjson = """

How are you?

I am fine. S
"""


def builder = new groovy.json.JsonBuilder()

def root = builder {
    "discriminator"  "http://vocab.lappsgrid.org/ns/media/jsonld"
    "payload"  {
        "metadata"  {
            "op"  "json2jsondsl"
            "template"  bratdsl
        }
        "@context"  "http://vocab.lappsgrid.org/context-1.0.0.jsonld"
        "sources"  ([lappsjson])
    }
}


println builder.toString()