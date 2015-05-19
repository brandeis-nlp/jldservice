{
  def idx = 0
  def parse = &$payload.views[-1].annotations.features[0].penntree
  text &$payload.text."@value" + (parse == null?"":"\n\n\n"+parse)
  entities (&$payload.views[-1].annotations.unique{it.start+" "+it.end}.select{&.features != null && (&.features.category != null || &.features.pos != null)}.foreach{
    [&.id == null?"T${idx++}":&.id, &.features.category != null?&.features.category.trim().toUpperCase():&.features.pos.trim().toUpperCase(), [[&.start.toInteger(), &.end.toInteger()]]]
  })
  relations (&$payload.views[-1].annotations.select{&.type=="http://vocab.lappsgrid.org/DependencyStructure"}.features.dependencies
             .flatten().foreach{
             [&.id, &.label, [["Governor", &.features.governor], ["Dependent", &.features.dependent]]]
             })
}