package connection;
/**
 * Created by hasee on 2017/11/27.
 */
public class GattyMessage {
    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "header:" + header + ",body:" + body;  
    }
}
