
# +------------------+------------------------------------------------
# | Standard targets |
# +------------------+

default: jar

.PHONY: clean
clean:
	cd src; make clean
	cd tests; make clean

install: jar
	cp *.jar /home/rebelsky/Web/Glimmer/Ushahidi/code

# +----------------+--------------------------------------------------
# | Custom targets |
# +----------------+

jar: simple-ushahidi-api.jar

# +------------------+------------------------------------------------
# | Individual Files |
# +------------------+

simple-ushahidi-api.jar: FORCE
	cd src; make            # Make sure all the class files are generated
	jar cvf $@ -C src edu   # Build the jar file

json.jar: FORCE
	jar cvf $@ -C src org   # Build the jar file

# +---------------+---------------------------------------------------
# | Miscellaneous |
# +---------------+

FORCE:
