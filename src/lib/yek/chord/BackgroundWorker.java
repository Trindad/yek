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

public class BackgroundWorker implements Runnable {
	Node node;

	public BackgroundWorker(Node n)
	{
		this.node = n;
	}

	public void run()
	{
		start();
	}

	public void start()
	{
		try {
			Thread.sleep(10000);
			System.out.println("successor "+this.node.routingTable.successor.ip + "/" + this.node.routingTable.successor.port);
			if (this.node.routingTable.predecessor != null) {
				System.out.println("predecessor "+this.node.routingTable.predecessor.ip + "/" + this.node.routingTable.predecessor.port);
			}
			this.node.checkPredecessor();
			this.node.stabilize();
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}