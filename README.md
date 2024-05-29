# Moxymind automation task

## Tech stack
Language and build tools:
* Gradle
* Kotlin (JVM v.21)

Test tools:
* jUnit
* Playwright

## Text execution

To run tests in terminal user can be run via gradlew task and execute them with commands:

For unix based OS:
```shell
./gradlew test
```
User can execute only UI/APU tests with `uiTest`/`apiTest` gradle task:
```shell
./gradlew uiTest
```
```shell
./gradlew apiTest
```
In the case of Windows OS:
```shell
./gradlew.bat test
```
or
```shell
./gradlew.bat uiTest
```
```shell
./gradlew.bat apiTest
```
In the case of UI tests, headless mode is true by default. In the case this is not wanted behaviour, user can alter this
by adding parameter into gradle task command.

For unix based OS:
```shell
./gradlew test -Dheadless=false
```

In the case of Windows OS:
```shell
./gradlew.bat test -Dheadless=false
```

Same applies for `uiTest` gradle task.

## Reports

Reports can be found in build folder:
``<root>/reports/tests/<task_name>/index.html``