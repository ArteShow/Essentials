.PHONY: build clean-build clean-build-linux copy-plugin

build:
	./gradlew build

clean-build:
	./gradlew clean build

clean-build-linux:
	chmod +x ./gradlew
	./gradlew clean build

copy-plugin:
	cp build/libs/Essentials-1.0.0.jar server/plugins
