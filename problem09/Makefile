default: run

compile:
	kotlinc ../utils/*.kt ./*.kt -include-runtime -d run.jar

run: compile
	java -jar run.jar

part1: run

part2: compile
	java -jar run.jar "part2"

clean:
	rm -f run.jar
