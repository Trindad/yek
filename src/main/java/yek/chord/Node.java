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

	public NodeInfo findSuccessor(BigInteger id)
	{
		if (this.info.id.compareTo(id) > 0 && this.routingTable.successor.id.compareTo(id) <= 0) {
			
			return this.routingTable.successor;
		}
	}

	public NodeInfo closestPrecedingNode(BigInteger id)
	{
		for (int i = 160; i > 0 ; i--) 
		{
			NodeInfo finger = this.routingTable.fingerTable[i].node;

			if (this.info.id.compareTo(finger.id) > 0 && finger.id.compareTo(id) < 0) 
			{
				return finger;
			}

			return this.info;	
		}
	}

	public void create()
	{
		this.routingTable = new RoutingTable();

		routingTable.successor = this.info;
	}

	public void join()
	{
	}

	public void stabilize()
	{
		
	}

}