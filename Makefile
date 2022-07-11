OBJECTS := $(shell find . -type f -name "*.java")

all:
	@javac $(OBJECTS)

clean:
	@find . -type f -name "*.class" -delete

store: all
	@java src.Store 127.0.0.0 2013 127.0.0.1 2013

test: all
	@java src.TestClient 127.0.0.1:2013 put test.txt
