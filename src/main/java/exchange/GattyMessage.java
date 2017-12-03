package exchange;

import java.util.Map;

/**
 * Created by hasee on 2017/11/27.
 */


public class GattyMessage {

    private Header header;
    private String protocol = "";
    private String username = "";
    private String password = "";
    private String host = "";
    private int port = 0;
    private String path = "";
    private Map<String, Object> attachment = null;

    public GattyMessage() {

    }

    public GattyMessage(Header header, String protocol, String host, int port, String path) {
        this.header = header;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public GattyMessage(String protocol, String host, int port, String path, Map<String, Object> attachment) {
        this.header = header;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        this.attachment = attachment;
    }

    public GattyMessage(String protocol, String username, String password, String host, int port, String path, Map<String, Object> attachment) {
        this.header = header;
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.path = path;
        this.attachment = attachment;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
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
}
