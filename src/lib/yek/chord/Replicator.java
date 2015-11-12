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
import java.util.ArrayDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.ArrayList;

public class Replicator implements Runnable {
	ArrayDeque<QueueItem> queue;
	Node node;

	public Replicator(Node n)
	{
		this.queue = new ArrayDeque<QueueItem>();
		this.node = n;
	}

	public void run()
	{
		while(true)
		{
			try
			{
				if (queue.isEmpty())
				{
					Thread.sleep(200);
					continue;
				}

				QueueItem item = queue.removeFirst();
				ArrayList<NodeInfo> nodes = new ArrayList<NodeInfo>();

				if (node.routingTable.successorList[0].id.equals(node.info.id)) {
					continue;
				}

				nodes.add(node.routingTable.successorList[0]);

				if (node.routingTable.successorList[1] != null &&
					!node.routingTable.successorList[1].id.equals(node.info.id) &&
					!nodes.contains(node.routingTable.successorList[1])) {
					nodes.add(node.routingTable.successorList[1]);
				}

				if (node.routingTable.predecessorList[0] != null &&
					!node.routingTable.predecessorList[0].id.equals(node.info.id) &&
					!nodes.contains(node.routingTable.predecessorList[0])) {
					nodes.add(node.routingTable.predecessorList[0]);
				}

				if (node.routingTable.predecessorList[1] != null &&
					!node.routingTable.predecessorList[1].id.equals(node.info.id) &&
					!nodes.contains(node.routingTable.predecessorList[1])) {
					nodes.add(node.routingTable.predecessorList[1]);
				}

				for (int i = 0; i < nodes.size(); i++) {
					if (item.operation.equals("delete"))
					{
						Request.removeReplica(nodes.get(i),item.key);
					}
					else
					{
						Request.saveReplica(nodes.get(i),this.node.info, item.data, item.key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void insert(String key, String operation, String data) throws Exception
	{
		QueueItem item = new QueueItem(key,operation,data);

		queue.add(item);
	}

	public void insert(String key, String operation) throws Exception
	{
		QueueItem item = new QueueItem(key,operation);

		queue.add(item);
	}
}
