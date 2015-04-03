package org.jldservice.db

import org.apache.commons.io.FileUtils

import java.util.concurrent.ConcurrentNavigableMap
import org.mapdb.*


/**
 * Created by lapps on 4/3/2015.
 */
class Jdbm {
    File fl;
    DBMaker maker;
    DB db;
    def Jdbm(){
        File root = FileUtils.toFile(Jdbm.class.getResource("/"));
        fl = new File(root, "mapdb");
        maker = DBMaker.newFileDB(fl).closeOnJvmShutdown();
        db = maker.make();
    }

    def open() {
        db = maker.make();
    }

    def close() {
        db.close();
    }

    def put(String mapName, String key, String val) {
        ConcurrentNavigableMap<String,String> map = db.getTreeMap(mapName);
        map.put(key, val);
        db.commit();
    }

    def get(String mapName, String key) {
        ConcurrentNavigableMap<String,String> map = db.getTreeMap(mapName);
        return map.get(key);
    }

    def remove(String mapName, String key) {
        ConcurrentNavigableMap<String,String> map = db.getTreeMap(mapName);
        String ret = map.remove(key);
        db.commit();
        return ret;
    }


    public static void main(String [] args) {
        Jdbm jdbm = new Jdbm();
        jdbm.put("map", "1", "2");
//        println(jdbm.get("map","1"));
    }
}

//
