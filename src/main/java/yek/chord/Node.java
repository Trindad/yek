package yek.chord;
import java.math.BigInteger;
import java.util.Hashtable;

public class Node {
	public NodeInfo info;
	public RoutingTable routingTable;
	public Hashtable<BigInteger, String> hashtable;

	public Node(String ip, BigInteger id)
	{
		this.info = new NodeInfo(id, ip);
		this.routingTable = new RoutingTable();
		this.hashtable = new Hashtable<BigInteger, String>();
	}

	public NodeInfo findSuccessor(BigInteger id)
	{
		if (this.info.id.compareTo(id) > 0 && this.routingTable.successor.id.compareTo(id) <= 0) {

			return this.routingTable.successor;
		}

		NodeInfo n = closestPrecedingNode(id);

		return Request.findSuccessor(n,id) ;
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
		}
		return this.info;
	}

	public void create()
	{
		this.routingTable = new RoutingTable();

		routingTable.successor = this.info;
	}

	public void join(NodeInfo n)
	{
		this.routingTable.predecessor = null;
		this.routingTable.successor = Request.findSuccessor(n,this.info.id);
	}

	public void stabilize()
	{
		NodeInfo s = this.routingTable.successor;

		NodeInfo x = Request.predecessor(s);

		if (x.id.compareTo(this.info.id) > 0 && x.id.compareTo(s.id) < 0)
		{
			this.routingTable.successor = x;
			s = x;
		}

		Request.notify(s, this.info);
	}

	public void notify(NodeInfo n)
	{
		if (this.routingTable.predecessor == null || (n.id.compareTo(this.routingTable.predecessor.id) > 0 && n.id.compareTo(this.info.id) < 0) )
		{
			this.routingTable.predecessor = n;
		}
	}

	public void fixFingers()
	{

	}

	public void put(String key,String data)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			NodeInfo n = findSuccessor(id);

			if (n.id.equals(this.info.id)) 
			{
				this.hashtable.put(id,data);
			}
			else
			{
				Request.store(n, key,data);
			}
		}
		catch (Exception e) { }
	}

	public String get()
	{
		return null;
	}
}
