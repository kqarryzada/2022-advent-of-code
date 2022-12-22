default: run

compile:
	kotlinc *.kt -include-runtime -d run.jar

run: compile
	java -jar run.jar

clean:
	rm -f run.jar
