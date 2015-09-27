lib:
	find src/lib -name "*.java" -print | xargs javac -d target/lib
compile_server: lib
	find src/server -name "*.java" -print | xargs javac -d target/server -classpath src/lib
compile_client: lib
	find src/client -name "*.java" -print | xargs javac -d target/client -classpath src/lib
run_initial_server:
	java -classpath target/server:target/lib Main --initial
run_server:
	java -classpath target/server:target/lib Main $(ip)
run_client:
	java -classpath target/client:target/lib Main $(ip) $(port)