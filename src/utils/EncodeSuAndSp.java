package utils;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author zc
 *         利用新浪的JS代码对密码进行编码
 */
public class EncodeSuAndSp {
    static ScriptEngineManager mgr = new ScriptEngineManager();
    static ScriptEngine engine = mgr.getEngineByExtension("js");
    static Invocable inv = (Invocable) engine;

    public static String getEncryptedP(String password, String servertime, String nonce) {
        String value1 = "";
        try {
            engine.eval(new FileReader(new File("js/encrypt.js")));
            value1 = String.valueOf(inv.invokeFunction("hex_sha1", password));
            value1 = String.valueOf(inv.invokeFunction("hex_sha1", value1));
            value1 = String.valueOf(inv.invokeFunction("hex_sha1", value1 + servertime + nonce));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value1;
    }


    public static String getEncodedUsername(String username) {
        String value1 = "";
        try {
            engine.eval(new FileReader(new File("js/encrypt.js")));
            value1 = String.valueOf(inv.invokeFunction("encode", username));
            System.out.println(value1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value1;
    }
}
