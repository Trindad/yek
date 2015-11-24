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

/**
 * Classe para enviar cópias
 * a um nó novo na rede.
 * Devemos esperar ele estabilizar antes de 
 * mandar os dados
 */
public class CopyScheduler implements Runnable {

	Node node;

	public CopyScheduler(Node n)
	{
		this.node = n;
	}

	public void run()
	{
		try {
			Thread.sleep(6000);

			node.queueEverything();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
