package protocol;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import exchange.URL;
import io.netty.channel.ChannelHandlerContext;

public class URLInvoker implements Invoker {
	
	private URL url;
	
	public URLInvoker(URL url) {
		// TODO Auto-generated constructor stub
		this.url = url;
	}
	
	@Override
	public Object invoke(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		String className = url.getClassName();
		String methodName = url.getMethodName();
		Map<String, Object> params = url.getAttachment();
		
		try {
			int index = 0;
			Class invokeClass = Class.forName("service." + className);
			Class[] paramsType = new Class[params.size()];
			Object[] paramsObject = new Object[params.size()];
			for (Entry<String, Object> entry : params.entrySet()) {
				paramsType[index] = entry.getValue().getClass();
				paramsObject[index++] = entry.getValue().toString();
			}
			Method method = invokeClass.getDeclaredMethod(methodName, paramsType);
			return method.invoke(invokeClass.newInstance(), paramsObject);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return null;
	}


}
