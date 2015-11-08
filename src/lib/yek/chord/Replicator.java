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
				Thread.sleep(10000);
				System.out.println("replication queue size: " + queue.size());

				if (queue.isEmpty()) 
				{
					continue;
				}

				QueueItem item = queue.removeFirst();

				if (item.operation.equals("delete")) 
				{
					Request.removeReplica(node.routingTable.successorList[0],item.key);
				}
				else
				{

					Request.saveReplica(node.routingTable.successorList[0],this.node.info, item.data, item.key);
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
