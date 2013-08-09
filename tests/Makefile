default: compile

.PHONY: clean
clean:
	rm -rf *.class

.PHONY: compile
compile:
	javac -cp ../simple-ushahidi-api.jar *.java

.PHONY: jar
jar:
	cd ..; make jar
