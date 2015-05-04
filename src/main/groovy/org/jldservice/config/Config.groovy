package org.jldservice.config

class Config {

    def config;

    public Config(conf) {
        def text = Config.class.getResource(conf).text
        config = new ConfigSlurper().parse(text)
    }

    String get(String keyEx) {
        def target = config;
        keyEx.split("\\.").eachWithIndex {
            key, i ->
                if (target != null)
                    target = target.get(key);
        }
        return target.toString().trim();
    }

    static Config defConfig = new Config("/jld.config");

    public static String getDef(String keyEx){
        return defConfig.get(keyEx).toString().trim();
    }
}