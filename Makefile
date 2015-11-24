lib:
	find src/lib -name "*.java" -print | xargs javac -d target/lib
compile_server: lib
	find src/server -name "*.java" -print | xargs javac -d target/server -classpath src/lib
compile_client: lib
	find src/client -name "*.java" -print | xargs javac -d target/client -classpath src/lib
run_initial_server: compile_server
	java -classpath target/server:target/lib Main
run_server: compile_server
	java -classpath target/server:target/lib Main $(ip) $(port)
run_client: compile_client
	java -classpath target/client:target/lib Main $(ip) $(port)
client_jar: compile_client
	jar -cfe yek_cli.jar Main -C target/client Main.class target/client/*
server_jar: compile_server
	jar -cfe yek.jar Main -C target/server Main.class target/server/*
