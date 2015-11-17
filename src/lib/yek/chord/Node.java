/**
 * Copyright 2015 Silvana Trindade and Maurício André Cinelli
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
 */
package yek.chord;
import java.util.*;
import java.math.BigInteger;
import java.util.Hashtable;
import java.io.IOException;

public class Node {

	public NodeInfo info;
	public RoutingTable routingTable;
	public Hashtable<BigInteger, Data> hashtable;
	public Hashtable<BigInteger, Replica> copies;
	private boolean isUpdatingFingerTable;
	public Replicator replicator;
	public boolean iAmNew;

	public Node(String ip, int port, BigInteger id)
	{
		this.info = new NodeInfo(id, ip, port);
		this.routingTable = new RoutingTable();
		this.hashtable = new Hashtable<BigInteger, Data>();
		this.copies = new Hashtable<BigInteger, Replica>();
		this.isUpdatingFingerTable = false;
		this.iAmNew = true;

		this.replicator = new Replicator(this);
		(new Thread(this.replicator)).start();
	}

	public NodeInfo findSuccessor(BigInteger id)
	{
		if (id.compareTo(this.info.id) > 0 && id.compareTo(this.routingTable.successorList[0].id) <= 0) {
			return this.routingTable.successorList[0];
		}

		NodeInfo n = null;

		if (isUpdatingFingerTable)
		{
			n = this.routingTable.successorList[0];
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

				if (this.info.id.compareTo(id) > 0) {
					if (finger.id.compareTo(id) < 0 || finger.id.compareTo(this.info.id) > 0) {
						return finger;
					}
				} else {
					if (finger.id.compareTo(id) < 0 && finger.id.compareTo(this.info.id) > 0) {
						return finger;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.info;
	}

	public void create()
	{
		routingTable.successorList[0] = this.info;
		routingTable.predecessorList[0] = null;
	}

	public void join(NodeInfo n)
	{
		this.routingTable.predecessorList[0] = null;
		this.routingTable.successorList[0] = Request.findSuccessor(n,this.info.id);
		this.fillFingerTable();
	}

	public void stabilize()
	{
		// System.out.println("Stabilizing...");
		NodeInfo s = this.routingTable.successorList[0];

		if (s == null)
		{
			return;
		}

		NodeInfo x = Request.predecessor(s);

		if( x != null)
		{
			if (s.id.equals(this.info.id))
			{
				this.routingTable.successorList[0] = x;
				s = x;
			}

			if (this.info.id.compareTo(s.id) > 0) {
				if (x.id.compareTo(s.id) < 0 || x.id.compareTo(this.info.id) > 0) {
					this.routingTable.successorList[0] = x;
					s = x;
				}
			} else {
				if (x.id.compareTo(s.id) < 0 && x.id.compareTo(this.info.id) > 0) {
					this.routingTable.successorList[0] = x;
					s = x;
				}
			}
		}

		if (!s.id.equals(this.info.id))
		{
			Request.notify(s, this.info);
		}
	}

	public void notify(NodeInfo n)
	{

		if (this.routingTable.predecessorList[0] == null)
		{
			this.routingTable.predecessorList[0] = n;
			this.guideNewGuy(n);
			return;
		}

		if (this.routingTable.predecessorList[0].id.compareTo(this.info.id) > 0)
		{
			if ((n.id.compareTo(this.routingTable.predecessorList[0].id) > 0 || n.id.compareTo(this.info.id) < 0) )
			{
				this.routingTable.predecessorList[0] = n;
				this.guideNewGuy(n);
			}

		} else {
			if ((n.id.compareTo(this.routingTable.predecessorList[0].id) > 0 && n.id.compareTo(this.info.id) < 0) )
			{
				this.routingTable.predecessorList[0] = n;
				this.guideNewGuy(n);
			}
		}

	}

	/**
	 * Verifica se o nó é novo na rede
	 * Se for envia os dados da sua responsabilidade agora
	 * e envia as cópias independentemente
	 */
	private void guideNewGuy(NodeInfo n)
	{
		boolean isHeNew = Request.askIfNew(n);

		if (isHeNew) {
			Request.sayHesNotNewAnymore(n);
			(new Thread(new NewNodeDataScheduler(this, n))).start();
		}
		
		
	}

	public void put(String key,String data)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			NodeInfo n = findSuccessor(id);

			if (n.id.equals(this.info.id))
			{
				Data t = new Data(key, data);
				this.hashtable.put(id,t);
			}
			else
			{
				Request.store(n, key,data);
			}

			this.replicator.insert(key,"put",data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void saveReplica(NodeInfo n, String key,String data)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);

			if (!this.hashtable.containsKey(id)) {
				Replica r = new Replica(n, key, data);

				this.copies.put(id,r);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
				if (this.hashtable.containsKey(id))
				{
					Data t = this.hashtable.get(id);
					return t == null ? "" : t.data;
				}
			}
			else
			{
				return Request.get(n, key);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
				Data t = new Data(key, data);
				this.hashtable.replace(id,t);
			}
			else
			{
				Request.update(n, key,data);
			}

			this.replicator.insert(key,"update",data);
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
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

			this.replicator.insert(key,"delete");
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public void removeReplica(String key)
	{
		try{
			Hash h = new Hash();

			BigInteger id = h.sha1(key);
			this.copies.remove(id);
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	public void checkPredecessor()
	{
		try {
			Request._make(this.routingTable.predecessorList[0], "heartbeat");
		} catch(IOException e) {
			TakeOverScheduler sch = new TakeOverScheduler(this, this.routingTable.predecessorList[0].id);
			new Thread(sch).start();
			this.routingTable.predecessorList[0] = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkSuccessors()
	{
		try {
			Request._make(this.routingTable.successorList[0], "heartbeat");
		} catch(IOException e) {
			this.routingTable.successorList[0] = this.routingTable.successorList[1];
		} catch (Exception e) {
			e.printStackTrace();
		}

		// caso especial?
		if (this.routingTable.successorList[0] == null) {
			this.routingTable.successorList[0] = this.info;
			return;
		}

		NodeInfo []newList = new NodeInfo[4];
		newList[0] = this.routingTable.successorList[0];

		NodeInfo current = this.routingTable.successorList[0];

		for (int i = 1; i <= 3; i++) {
			NodeInfo successor = Request.successor(current);
			if (successor == null || successor.id.compareTo(this.info.id) == 0) {
				break;
			}

			boolean found = false;
			for (int j = 0; j < i; j++) {
				if (successor.id.compareTo(newList[j].id) == 0) {
					found = true;
					break;
				}
			}

			if (found) {
				break;
			}

			newList[i] = successor;
			current = successor;
		}
		this.routingTable.successorList = newList;

		// System.out.println("List of successors:");
		// for (int j = 0; j < 4; j++) {
		// 	if (this.routingTable.successorList[j] == null) {
		// 		continue;
		// 	}

		// 	String ip = this.routingTable.successorList[j].ip;
		// 	int port = this.routingTable.successorList[j].port;
		// 	System.out.println(ip + "/" + port);
		// }
		// System.out.println("----------------------------");
	}
}
