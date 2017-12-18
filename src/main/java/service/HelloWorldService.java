package service;

/**
 * Created by hasee on 2017/12/18.
 */
public class HelloWorldService implements HelloWorld {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
