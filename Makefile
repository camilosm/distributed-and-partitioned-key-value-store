OBJECTS := $(shell find . -type f -name "*.java")

all:
	@javac $(OBJECTS)

clean: terminate
	@find . -type f -name "*.class" -delete
	@rm -rf store_* received

store: all
	@java src.Store 224.0.0.1 2013 127.0.0.1 2013 &
	@java src.Store 224.0.0.1 2013 127.0.0.2 2013 &
	@java src.Store 224.0.0.1 2013 127.0.0.3 2013 &

put: all
	@java src.TestClient 127.0.0.1:2013 put README.md

get: all
	@java src.TestClient 127.0.0.1:2013 get 93fa44bc8925f5fe1c081688cff0c9b4e90c26622faadeefe8e11a705e257616

delete: all
	@java src.TestClient 127.0.0.1:2013 delete 93fa44bc8925f5fe1c081688cff0c9b4e90c26622faadeefe8e11a705e257616

view: all
	@java src.TestClient 127.0.0.1:2013 view

join: all
	@java src.TestClient 127.0.0.1:2013 join

leave: all
	@java src.TestClient 127.0.0.1:2013 leave

terminate:
	@if jps | grep -q Store; then kill -9 $(shell jps | grep Store | tr -d 'Store'); fi;
