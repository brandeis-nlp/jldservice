{
  def idx = 1
  text &$payload.text."@value"
  entities (&$payload.views[-1].annotations.unique{it.start+" "+it.end}.select{&.features.category != null}.foreach{
    ["T${idx++}", &.features.category.trim().toUpperCase(), [[&.start.toInteger(), &.end.toInteger()]]]
  })	
}