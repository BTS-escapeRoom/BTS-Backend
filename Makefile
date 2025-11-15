run:
	@echo "Starting Spring Boot with 'local' profile (Gradle)..."
	./gradlew bootRun --args='--spring.profiles.active=local'