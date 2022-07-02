OBJECTS := $(shell find . -type f -name "*.java")

all:
	@javac $(OBJECTS)

clean:
	@find . -type f -name "*.class" -delete

test: all
	@java src.TestClient 127.0.0.1 put
