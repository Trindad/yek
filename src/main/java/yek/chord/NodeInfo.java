package yek.chord;
import java.math.BigInteger;

public class NodeInfo {
	
	public BigInteger id;
	public String ip;

	public NodeInfo(BigInteger id, String ip)
	{
		this.ip = ip;
		this.id = id;
	}
}