package exchange;

import java.util.HashMap;
import java.util.Map;

public class URL {
    private String protocol = "";
    private String username = "";
    private String password = "";
    private String host = "";
    private int port = 0;
    private String path = "";
    private String className;
    private String methodName;
    private Map<String, Object> attachment = null;
    
    public URL() {
    	
    }
    
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, Object> getAttachment() {
		return attachment;
	}
	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	//protocol://username:password@host:port/path
	public static URL translate(String url) {
		URL res = new URL();
		int start = 0, last = url.indexOf(":");
		String protocol = url.substring(0, last);
		start = last + 1;
		String username = "";
		String password = "";
		if (url.contains("@")) {
			last = url.indexOf(":", start);
			username = url.substring(start, last);
			start = last + 1;
			last = url.indexOf("@");
			password = url.substring(start, last);
			start = last + 1;
		}
		String host = "";
		String port = "";
		last = url.indexOf(":", start);
		host = url.substring(start + 2, last);
		start = last + 1;
		last = url.indexOf("/", start);
		port = url.substring(start, last);
		String path = "";
		start = last + 1;
		last = url.indexOf("?", start);
		if (last == -1) {
			last = url.length();
		}
		path = url.substring(start, last);


		res.setProtocol(protocol);
		res.setHost(host);
		res.setUsername(username);
		res.setPassword(password);
		res.setPath(path);
		res.setPort(Integer.valueOf(port));

		if (url.length() != last) {
			Map<String, Object> map = new HashMap<>();
			start = last + 1;
			while(start < url.length()) {
				String key;
				String value;
				last = url.indexOf("=", start);
				key = url.substring(start, last);
				start = last + 1;
				last = url.indexOf("&", start);
				if (last == -1) {
					last = url.length();
				}
				value = url.substring(start, last);
				start = last + 1;
				map.put(key, value);
			}
			res.setAttachment(map);
		}

		return res;
	}

	//protocol://username:password@class/method
	public static String translate (URL url) {
		StringBuilder sb = new StringBuilder();
		sb.append(url.getProtocol());
		sb.append("://");
		if (!"".equals(url.getUsername())) {
			sb.append(url.getUsername());
			sb.append(":");
			sb.append(url.getPassword());
			sb.append("@");
		}
		sb.append(url.getHost());
		sb.append(":");
		sb.append(url.getPort());
		sb.append("/");
		sb.append(url.getPath());
		Map<String, Object> map = url.getAttachment();
		boolean first = true;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (first) {
				sb.append("?");
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(key + "=" + value.toString());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Protocol: " + protocol + "\n" +
				"Username: " + username + "\n" +
				"Password: " + password + "\n" +
				"Host: " + host + "\n" +
				"Port: " + port + "\n" +
				"Path: " + path + "\n");
		for (Map.Entry<String, Object> entry : attachment.entrySet()) {
			sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
		}
		return sb.toString();
	}
}
