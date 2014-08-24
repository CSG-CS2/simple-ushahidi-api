default: compile

CLASSPATH=".:../simple-ushahidi-api.jar:../json.jar"

%.class: %.java
	javac -cp $(CLASSPATH) $<

%.run: %.class
	java -cp $(CLASSPATH) $*

.PHONY: clean
clean:
	rm -rf *.class

.PHONY: compile
compile:
	javac -cp $(CLASSPATH) *.java

.PHONY: jar
jar:
	cd ..; make jar

.PHONY: harness
harness: TestHarnessClientExperiment.class
	java -cp $(CLASSPATH) TestHarnessClientExperiment

.PHONY: sandbox
sandbox: WebClientExperiment.class
	java -cp $(CLASSPATH) \
		WebClientExperiment http://ushahidi1.grinnell.edu/sandbox

.PHONY: global
global: WebClientExperiment.class
	java -cp $(CLASSPATH) \
		WebClientExperiment http://ushahidi1.grinnell.edu/global

.PHONY: global-array
global-array: WebClientArrayExperiment.class
	java -cp $(CLASSPATH) \
		WebClientArrayExperiment http://ushahidi1.grinnell.edu/global

.PHONY: report
report: WebReporterExperiment.class
	java -cp $(CLASSPATH) WebReporterExperiment
	
