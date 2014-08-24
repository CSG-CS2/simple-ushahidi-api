default: compile

%.class: %.java
	javac -cp ../simple-ushahidi-api.jar $<

.PHONY: clean
clean:
	rm -rf *.class

.PHONY: compile
compile:
	javac -cp ../simple-ushahidi-api.jar *.java

.PHONY: jar
jar:
	cd ..; make jar

.PHONY: harness
harness: TestHarnessClientExperiment.class
	java -cp ".:../simple-ushahidi-api.jar:../json.jar" \
		TestHarnessClientExperiment

.PHONY: sandbox
sandbox: WebClientExperiment.class
	java -cp ".:../simple-ushahidi-api.jar:../json.jar" \
		WebClientExperiment http://ushahidi1.grinnell.edu/sandbox

.PHONY: global
global: WebClientExperiment.class
	java -cp ".:../simple-ushahidi-api.jar:../json.jar" \
		WebClientExperiment http://ushahidi1.grinnell.edu/global

.PHONY: global-array
global-array: WebClientArrayExperiment.class
	java -cp ".:../simple-ushahidi-api.jar:../json.jar" \
		WebClientArrayExperiment http://ushahidi1.grinnell.edu/global

.PHONY: report
report: WebReporterExperiment.class
	java -cp ".:../simple-ushahidi-api.jar:../json.jar" \
                WebReporterExperiment
	
