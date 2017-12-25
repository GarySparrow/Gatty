package exchange;

import common.GattyConstant;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by hasee on 2017/12/19.
 */
public class URLtest {

    @Test
    public void translate2Str() {
        Scanner scan = new Scanner(System.in);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Gatty");
        URL url = new URL("DEFAULT", GattyConstant.REMOTEIP, GattyConstant.PORT,
                "HelloWorldService", "sayHello", map);
        try {
            System.out.println(URL.translate(url));
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void translate2URL() {
        Scanner scan = new Scanner(System.in);
        String str = "DEFAULT://" + GattyConstant.REMOTEIP + ":" + GattyConstant.PORT
                + "/HelloWorldService:sayHello?name=Gatty";
        try {
            System.out.println(URL.translate(str).toString());
        } catch (URLTranslateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tempTest() {
        File file = new File("src/main/java");
        System.out.println();
    }
}
