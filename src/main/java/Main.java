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
import yek.*;

class Main {
	public static void main(String[] args) {
		// for(int i = 0; i < args.length;i++)
		if (args.length < 1) 
		{
			System.out.println("Wrong number of arguments");
			System.exit(1);
		}
		// System.out.println(args[0]);

		boolean initial = args[0].equals("--initial");

		Server s = new Server();

		s.start(initial,args[0]);
	}
}