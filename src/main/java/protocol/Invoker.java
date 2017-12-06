package protocol;

import java.lang.reflect.Method;

import exchange.URL;

public interface Invoker {
	Object invoke(URL url);
}
