package common;

public enum MessageType {
	LOGIN_REQ((byte)1), LOGIN_RESP((byte)2), HEARTBEAT_REQ((byte)3), HEARTBEAT_RESP((byte)4);
	
	private byte v;
	
	private MessageType (byte v) {
		this.v = v;
	}
	
	public byte value() {
		return v;
	}
}
