package yek.chord;
import java.math.BigInteger;
import java.util.Hashtable;

public class Node {
	public NodeInfo info;
	public RoutingTable routingTable;
	public Hashtable<BigInteger, String> hashtable;
	private boolean isUpdatingFingerTable;

	public Node(String ip, BigInteger id)
	{
		this.info = new NodeInfo(id, ip);
		this.routingTable = new RoutingTable();
		this.hashtable = new Hashtable<BigInteger, String>();
		this.isUpdatingFingerTable = false;
	}

	public NodeInfo findSuccessor(BigInteger id)
	{
		if (id.compareTo(this.info.id) > 0 && id.compareTo(this.routingTable.successor.id) <= 0) {
			return this.routingTable.successor;
		}

		NodeInfo n = null;

		if (isUpdatingFingerTable) 
		{
			n = this.routingTable.successor;
		}
		else
		{
			n = closestPrecedingNode(id);
		}

		if (n.id.equals(this.info.id)) 
		{
			return n;
		}

		return Request.findSuccessor(n,id) ;
	}

	public NodeInfo closestPrecedingNode(BigInteger id)
	{
		try {
			for (int i = 159; i >= 0 ; i--)
			{
				if (this.routingTable.fingerTable[i] == null) {
					continue;
				}

				NodeInfo finger = this.routingTable.fingerTable[i].node;

				if (this.info.id.compareTo(finger.id) > 0 && finger.id.compareTo(id) < 0)
				{
					return finger;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.info;
	}

	public void create()
	{
		routingTable.successor = this.info;
		routingTable.predecessor = null;
	}

	public void join(NodeInfo n)
	{
		this.routingTable.predecessor = null;
		this.routingTable.successor = Request.findSuccessor(n,this.info.id);
		this.fillFingerTable();
	}

	public void stabilize()
	{
		System.out.println("Stabilizing...");
		NodeInfo s = this.routingTable.successor;

		if (s == null)
		{
			return;
		}

		NodeInfo x = Request.predecessor(s);

		if( x != null)
		{
			if (s.id.equals(this.info.id) || (x.id.compareTo(this.info.id) > 0 && x.id.compareTo(s.id) < 0))
			{
				this.routingTable.successor = x;
				s = x;
			}
		}

		if (!s.id.equals(this.info.id))
		{
			Request.notify(s, this.info);
		}

	}

	public void notify(NodeInfo n)
	{
		System.out.println("new predecessor "+n.id);

		if (this.routingTable.predecessor == null || (n.id.compareTo(this.routingTable.predecessor.id) > 0 && n.id.compareTo(this.info.id) < 0) )
		{
			System.out.println("not.......................");
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

	public String get(String key)
	{
		try
		{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			NodeInfo n = findSuccessor(id);

			if (n.id.equals(this.info.id))
			{
				return this.hashtable.get(id);
			}
			else
			{
				return Request.get(n, key);
			}
		}
		catch (Exception e) { }

		return "";
	}

	public void fillFingerTable()
	{
		isUpdatingFingerTable = true;
		BigInteger n = this.info.id;

		for (int i = 0; i < 160; i++)
		{

			this.routingTable.fingerTable[i] = new Finger();
			BigInteger toFind = new BigInteger(n.toString());
			toFind = toFind.add((new BigInteger("2")).pow(i)).remainder((new BigInteger("2")).pow(160));

			this.routingTable.fingerTable[i].node = findSuccessor(toFind);
		}

		isUpdatingFingerTable = false;
	}

	public void update(String key,String data)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			NodeInfo n = findSuccessor(id);

			if (n.id.equals(this.info.id))
			{
				this.hashtable.replace(id,data);
			}
			else
			{
				Request.update(n, key,data);
			}
		}
		catch (Exception e) { }
	}

	public void delete(String key)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			NodeInfo n = findSuccessor(id);

			if (n.id.equals(this.info.id))
			{
				this.hashtable.remove(id);
			}
			else
			{
				Request.delete(n, key);
			}
		}
		catch (Exception e) { }
	}

	public void checkPredecessor()
	{
		try {
			Request.make(this.routingTable.predecessor, "heartbeat");
		} catch(IOException e) {
			this.routingTable.predecessor = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
