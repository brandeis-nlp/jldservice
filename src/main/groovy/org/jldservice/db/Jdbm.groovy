package org.jldservice.db

import org.apache.commons.io.FileUtils
import org.mapdb.DB
import org.mapdb.DBMaker

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.security.Security
import java.util.concurrent.ConcurrentNavigableMap

/**
 * Created by lapps on 4/3/2015.
 */
class Jdbm {
    File fl;
    DBMaker maker;
    DB db;
    def Jdbm(){
        File root = FileUtils.toFile(Jdbm.class.getResource("/"));
        fl = new File(new File(root, "db"), "org.jldservice.db.Jdbm");
        println "db.local.file = " + fl
        if(!fl.exists())
            fl.createNewFile();
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
        println "\n----------------------------\nPUT:" + mapName + "\n VAL:" + val;
        db.commit();
    }

    def get(String mapName, String key) {
        ConcurrentNavigableMap<String,String> map = db.getTreeMap(mapName);
        println "\n----------------------------\nGET:" + mapName;
        return map.get(key);
    }

    def remove(String mapName, String key) {
        ConcurrentNavigableMap<String,String> map = db.getTreeMap(mapName);
        String ret = map.remove(key);
        db.commit();
        return ret;
    }


    static def decrypt(String cipherText) {
        // This statement is not needed since the SunJCE is
        // (statically) installed as of SDK 1.4.  Do this to
        // dynamically install another provider.
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        // Generate a secret key for a symmetric algorithm and
        // create a Cipher instance. DESede key size is always
        // 168 bits. Other algorithms, like "blowfish", allow
        // for variable lenght keys.
        KeyGenerator keyGenerator =
                KeyGenerator.getInstance("DESede");
        keyGenerator.init(168);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DESede");
        // Store the string as an array of bytes.  You should
        // specify the encoding method for consistent encoding
        // and decoding across different platforms.
        // String pass = "I sure love working with the JCE.";
        byte[] cipherBytes = cipherText.getBytes("UTF8");
        // Reinitialize the cipher an decrypt the byte array
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        String decryptedText = new String(decryptedBytes, "UTF8");
        return decryptedText;
    }


    static def encrypt(String text) {
        // This statement is not needed since the SunJCE is
        // (statically) installed as of SDK 1.4.  Do this to
        // dynamically install another provider.
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        // Generate a secret key for a symmetric algorithm and
        // create a Cipher instance. DESede key size is always
        // 168 bits. Other algorithms, like "blowfish", allow
        // for variable lenght keys.
        KeyGenerator keyGenerator =
                KeyGenerator.getInstance("DESede");
        keyGenerator.init(168);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DESede");
        // Store the string as an array of bytes.  You should
        // specify the encoding method for consistent encoding
        // and decoding across different platforms.
        // String pass = "I sure love working with the JCE.";
        byte[] clearTextBytes = text.getBytes("UTF8");
        // Initialize the cipher and encrypt this byte array
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBytes = cipher.doFinal(clearTextBytes);
        String cipherText = new String(cipherBytes, "UTF8");
        return cipherText;
    }

    def init(String name, String key) {
        String json = this.class.getResource("/db/"+name+"#"+key+".json").text;
        // println json
        put(name, key, json);
    }


    // http://pro.jsonlint.com/
    public static void main(String [] args) {
        Jdbm jdbm = new Jdbm();
        // ServiceType, Producer, Version, Url, P
        jdbm.init("lapps.html", "services");
//        println jdbm.remove("brat.html", "results");
    }
}

//
