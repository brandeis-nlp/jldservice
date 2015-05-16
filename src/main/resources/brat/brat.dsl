{
  def idx = 1
  text &$payload.text."@value"
  entities (&$payload.views[-1].annotations.unique{it.start+" "+it.end}.select{&.features != null && (&.features.category != null || &.features.pos != null)}.foreach{
    ["T${idx++}", &.features.category != null?&.features.category.trim().toUpperCase():&.features.pos.trim().toUpperCase(), [[&.start.toInteger(), &.end.toInteger()]]]
  })
}