# yoja-blueprint-hello

Minimal blueprint for the [yoja](https://github.com/easygoingapi) framework. It wires a single REST endpoint to a frontend page in the fewest possible lines — the starting point for any new yoja project.

## What it shows

| Layer | File | Concept |
|---|---|---|
| Backend | `Hello.java` | `HttpRouter` setup, serving JAR resources, declaring a REST route |
| Frontend | `index.html` | yoja-web page, controller binding via `yw-controler` |
| Frontend | `HelloControler.js` | `httpClient.get()`, DOM update with `section.firstTag()` |

## Project structure

```
yoja-blueprint-hello/
├── src/main/java/blueprint/
│   └── Hello.java                 # Entry point: router + HTTP server
└── src/main/webapp/blueprint/
    ├── index.html                 # Single page
    └── HelloControler.js          # Frontend controller
```

## Prerequisites

- Java 25+
- yoja dependencies available in Maven local (`~/.m2`):
  - `com.easygoingapi:yoja-web:0.0.0-SNAPSHOT`
  - `com.easygoingapi:yoja-http-server:0.0.0-SNAPSHOT`

## Gradle commands

```bash
# Start the application (dev mode, uses logback-test.xml)
./gradlew run

# Build the project (compile + resources)
./gradlew build

# Package as a self-contained ZIP with startup scripts
./gradlew distZip

# Clean build outputs
./gradlew clean

# List all available tasks
./gradlew tasks
```

Once running, open: **http://localhost:9090/index.html**

The page calls `GET /hello` and displays the response (`hello, yoja`).

## How it works

**Backend** (`Hello.java`) builds an `HttpRouter` that:
1. Serves the yoja-web JavaScript framework from its JAR at `/yoja/*`
2. Serves the `blueprint/` resources from the application JAR at `/*`
3. Exposes `GET /hello` returning a plain-text response

```java
HttpRouter.builder()
    .webResource(WebApp.of(WebApp.Type.jar, "com.easygoingapi.yoja.web", "/yoja"), "/*")
    .webResource(WebApp.jar("blueprint"), "/*")
    .webService(HttpMethod.GET, "/hello", r -> r.response().send("hello, yoja"))
    .build();
```

**Frontend** (`HelloControler.js`) runs when the page is ready, calls the endpoint, and writes the response into the DOM:

```js
section.pageReady(() => {
    yojaWebApi.httpClient
        .get({ url: '/hello' })
        .then(res => {
            section.firstTag('.message').textContent = res.body;
        });
});
```

## Distribution (deployment)

```bash
./gradlew distZip
unzip build/distributions/yoja-blueprint-hello.zip -d /opt/yoja-hello
/opt/yoja-hello/yoja-blueprint-hello/bin/yoja-blueprint-hello
```
