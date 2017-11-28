package connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee on 2017/11/27.
 */
public final class Header {
    private int crcCode = 0xabef0101;
    private int length;
    private long sessionId;
    private byte type;
    private byte priority;
    private Map<String, Object> external = new HashMap<>();

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getExternal() {
        return external;
    }

    public void setExternal(Map<String, Object> external) {
        this.external = external;
    }
}
