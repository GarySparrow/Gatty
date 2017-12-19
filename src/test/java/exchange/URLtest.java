package exchange;

import common.GattyConstant;
import org.junit.Test;

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
        URL url = new URL();
        url.setHost(GattyConstant.REMOTEIP);
        url.setPort(GattyConstant.PORT);
        url.setProtocol("DEFAULT");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Gatty");
        url.setAttachment(map);
        url.setPath("HelloWorld:sayHello");
        System.out.println(URL.translate(url));
    }

    @Test
    public void translate2URL() {
        Scanner scan = new Scanner(System.in);
        String str = "DEFAULT://" + GattyConstant.REMOTEIP + ":" + GattyConstant.PORT
                + "/HelloWorld:sayHello?name=Gatty";
        System.out.println(URL.translate(str).toString());
    }
}
