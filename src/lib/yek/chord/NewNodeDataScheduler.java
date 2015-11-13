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
import java.util.Enumeration;
import java.math.BigInteger;
import java.util.ArrayDeque;

public class NewNodeDataScheduler implements Runnable {

	Node node;
	NodeInfo target;

	public NewNodeDataScheduler(Node n, NodeInfo target)
	{
		this.node = n;
		this.target = target;
	}

	public void run()
	{
		try {
			Thread.sleep(6000);

			for (Enumeration<BigInteger> it = node.hashtable.keys(); it.hasMoreElements();)
			{
				BigInteger key = it.nextElement();
				Data data = node.hashtable.get(key);

				NodeInfo n = node.findSuccessor(key);
				if (n.id.compareTo(target.id) == 0)
				{
					Request.store(target,data.key,data.data);
				}

				node.hashtable.remove(key);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
