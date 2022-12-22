default: compile

compile: 
	kotlinc Main.kt -include-runtime -d run.jar

run: compile
	java -jar run.jar

clean: 
	rm -f run.jar
