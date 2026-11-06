.PHONY: build build-26.1 build-1.21.11 clean-build clean-build-linux copy-plugin

build: build-26.1

build-26.1:
	./gradlew clean build -PpaperVersion=26.1.2.build.+ -PjavaRelease=25 --refresh-dependencies
	mkdir -p build/releases
	cp build/libs/Essentials-1.0.0.jar build/releases/Essentials-26.1.jar

build-1.21.11:
	./gradlew clean build -PpaperVersion=1.21.11-R0.1-SNAPSHOT -PjavaRelease=21
	mkdir -p build/releases
	cp build/libs/Essentials-1.0.0.jar build/releases/Essentials-1.21.11.jar

clean-build:
	./gradlew clean build

clean-build-linux:
	chmod +x ./gradlew
	./gradlew clean build

copy-plugin:
	cp build/libs/Essentials-1.0.0.jar server/plugins