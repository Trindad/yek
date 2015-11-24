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
import java.util.Scanner;
import java.math.BigInteger;
import yek.chord.*;

class Main {
	public static void main(String[] args) {

		if (args.length < 2)
		{
			System.out.println("Wrong number of arguments");
			System.exit(1);
		}

		NodeInfo n = null;

		try {
			int port = Integer.parseInt( args[1] );
			Hash h = new Hash();
			BigInteger b = h.sha1(args[0] + "/" + port);
			n = new NodeInfo(b, args[0], port);

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Node not running YEK");
			System.exit(0);
			return;
		}

		Scanner keyboard = new Scanner(System.in);

		for (Integer i = 0; i < 1000; i++) {
			String str = "key_" + i.toString();

			Request.store(n,str,"real value");
		}

		for (Integer i = 0; i < 100; i++) {
			String str = "key_" + i.toString();
			String res = Request.get(n,str);

			if ("real value".equals(res) == false) {
				System.out.println("CRAP! " + res);
			}
		}

		/**
		 * Funções que o banco consegue executar
		 */
		while(true)
		{
			System.out.println("Options:");
			System.out.println("- get");
			System.out.println("- put");
			System.out.println("- delete\n");
			System.out.print("> ");
			String option = keyboard.next();
			String key = "";

			switch (option)
			{
				case "put":
					System.out.print("key: ");
					key = keyboard.next();
					System.out.print("value: ");
					String value = keyboard.next();

					Request.store(n,key,value);

					break;
				case "get":
					System.out.print("key: ");
					key = keyboard.next();

					System.out.println(Request.get(n,key));
					break;
				case "delete":
					System.out.print("key: ");
					key = keyboard.next();

					Request.delete(n,key);
					break;
				case "exit":
					System.exit(1);
				default:
					break;
			}
		}
	}
}
