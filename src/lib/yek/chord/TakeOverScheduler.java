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

public class TakeOverScheduler implements Runnable {

	Node node;
	BigInteger id;

	public TakeOverScheduler(Node n, BigInteger id)
	{
		this.node = n;
		this.id = id;
	}

	public void run()
	{
		try {
			Thread.sleep(7000);
			for (Enumeration<BigInteger> it = node.copies.keys(); it.hasMoreElements();)
			{
				BigInteger key = it.nextElement();
				Replica r = node.copies.get(key);

				if (r.info.id.equals(id)) {
					node.put(r.key, r.data);
					node.copies.remove(key);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
