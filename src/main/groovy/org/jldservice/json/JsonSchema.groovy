package org.jldservice.json

import com.fasterxml.jackson.databind.ObjectMapper
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
            e.printStackTrace()
            return clz.toString()
        }
    }

//    static def toJsonSchema2Pojo(clz) {
//        JCodeModel codeModel = new JCodeModel();
//        URL source = new URL("file:///path/to/my/schema.json");
//        new SchemaMapper().generate(codeModel, "ClassName", "com.example", source);
//        codeModel.build(new File("output"));
//    }
}
