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
		boolean initial = args.length == 0;//verifica se é nó incial

		Server s = new Server();
		int port = 32000;//porta padrão

		//verifica se existe a porta como segundo parâmetro de entrada
		if (args.length > 1) 
		{
			port = Integer.parseInt(args[1]);
		}

		s.start(initial,initial ? "" : args[0], port);

	}
}
