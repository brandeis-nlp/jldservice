package org.jldservice.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper

//
// http://stackoverflow.com/questions/17783909/create-json-schema-from-java-class
//
class JsonSchema{
    static def toJsonSchema (clz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
            mapper.acceptJsonFormatVisitor(clz, visitor);
            com.fasterxml.jackson.module.jsonSchema.JsonSchema schema = visitor.finalSchema();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
        }catch (java.lang.StackOverflowError e){
            return e.toString()
        }
    }
}
