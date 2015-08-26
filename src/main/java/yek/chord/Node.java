package yek.chord;
import java.math.BigInteger;

public class Node {
	NodeInfo info;
	RoutingTable routingTable;

	public Node(String ip, BigInteger id)
	{
		this.info.id = id;
		this.info.ip = ip;
	}


}