package remote;

import java.lang.reflect.Method;

public interface Invoker {
	void invoke(Class T, Method method);
}
