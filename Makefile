OBJECTS := $(shell find . -type f -name "*.java")

all:
	@javac $(OBJECTS)

clean:
	@find . -type f -name "*.class" -delete

store: all
	@java src.Store 127.0.0.0 2013 127.0.0.1 2013

put: all
	@java src.TestClient 127.0.0.1:2013 put README.md

get: all
	@java src.TestClient 127.0.0.1:2013 get 93fa44bc8925f5fe1c081688cff0c9b4e90c26622faadeefe8e11a705e257616

delete: all
	@java src.TestClient 127.0.0.1:2013 delete 93fa44bc8925f5fe1c081688cff0c9b4e90c26622faadeefe8e11a705e257616
