{
         def idx = 0
         def parse = &$payload.views[-1].annotations.features[0].penntree
         def coref = &$payload.views[-1].annotations.select{&.type=="http://vocab.lappsgrid.org/Coreference"}.features.mentions
         def markables = &$payload.views[-1].annotations.select{&.type=="http://vocab.lappsgrid.org/Markable" && coref.toString().contains(&.id)}

         text &$payload.text."@value" + (parse == null?"":"\n\n\n"+parse)

         relations (&$payload.views[-1].annotations.select{&.type=="http://vocab.lappsgrid.org/DependencyStructure"}.features.dependencies
                    .flatten().foreach{
                    ["D${idx++}", &.label, [["Governor", &.features.governor], ["Dependent", &.features.dependent]]]
                    })

         equivs (&$payload.views[-1].annotations.select{&.type=="http://vocab.lappsgrid.org/Coreference"}.features
                    .flatten().foreach{
                                     ["*", "Coreference", &.mentions[0], &.mentions[1]]
                                     })

         entities (&$payload.views[-1].annotations.unique{&.start+" "+&.end}.select{&.features != null && (&.features.category != null || &.features.pos != null)}.foreach{
           [&.id == null?"T${idx++}":&.id, &.features.category != null?&.features.category.trim().toUpperCase():&.features.pos.trim().toUpperCase(), [[&.start.toInteger(), &.end.toInteger()]]]
         } + markables.foreach{
           [&.id == null?"M${idx++}":&.id, "Mention", [[&.start.toInteger(), &.end.toInteger()]]]
         })
}