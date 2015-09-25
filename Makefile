compile_server:
	find src/main/java/ -name "*.java" -print | xargs javac -d target
run_initial_server:
	java -classpath target Main --initial
run_server:
	java -classpath target Main $(ip)