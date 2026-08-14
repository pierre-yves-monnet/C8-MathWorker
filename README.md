# C8-MathWorker

A Camunda 8 Spring Boot job worker application, named **mathWorker**, exposing three simple
mathematical operations via `@JobWorker`-annotated methods, built on `camunda-spring-boot-starter`
8.9.16 (Spring Boot 4.0.7).

A matching BPMN process, `mathWorker` (`src/test/resources/mathWorker.bpmn`), calls the three
workers in sequence and ends with a user task.

## Workers

| Job type         | Signature                     | Behaviour                                                                 |
|------------------|--------------------------------|----------------------------------------------------------------------------|
| `addition`       | `addition(int a, int b)`       | Completes the job with `result = a + b`                                   |
| `multiplication` | `multiplication(int a, int b)` | Completes the job with `result = a * b`                                   |
| `division`       | `division(int a, int b)`       | Completes the job with `result = a / b`; throws BPMN error `DIVISION_BY_ZERO` when `b == 0` |

Each worker reads two integer process variables, `a` and `b`, and writes a single output variable,
`result`, back onto the process instance. In the BPMN process, each service task defaults `a` to
`10` and `b` to `5` via a FEEL input mapping when the process didn't supply them.

## Project layout

```
src/main/java/org/camunda/worker/mathwoker/
├── MathWorkerApplication.java     # @SpringBootApplication entry point
└── worker/
    ├── AdditionWorker.java        # @JobWorker(type = "addition")
    ├── MultiplicationWorker.java  # @JobWorker(type = "multiplication")
    └── DivisionWorker.java        # @JobWorker(type = "division")
src/main/resources/application.yaml
src/test/resources/
├── mathWorker.bpmn                # the process calling all three workers
└── review-results.form            # form linked to the closing user task
```

Each worker is a `@Component` bean with one method annotated
[`@JobWorker`](https://github.com/camunda/camunda/blob/main/clients/spring/annotations/src/main/java/io/camunda/client/annotation/JobWorker.java);
the Camunda Spring Boot Starter registers it against the configured cluster automatically at
startup — no manual `CamundaClient` wiring required. Method parameters are bound with `@Variable`,
and the return value auto-completes the job as the output variable map. `DivisionWorker` throws
`CamundaError.bpmnError(...)` for division-by-zero, since that's a modelled business outcome
rather than a transient failure.

## Prerequisites

- Java 17+
- Maven 3.9+
- A running Camunda 8 cluster reachable locally — for example
  [c8run](https://github.com/camunda/camunda/tree/main/c8run), exposing:
  - gRPC gateway on `localhost:26500`
  - REST gateway on `localhost:8080`

## Build

```bash
mvn -f "C:\dev\intellij\consulting\c8-MathWorker\pom.xml" clean package
```

This produces an executable Spring Boot jar at `target/mathWorker.jar`.

## Run

```bash
java -jar "C:\dev\intellij\consulting\c8-MathWorker\target\mathWorker.jar"
```

The application connects to `http://localhost:26500` (gRPC) and `http://localhost:8080` (REST) by
default, in `self-managed` mode with no authentication — see `src/main/resources/application.yaml`.
Override with environment variables if your local cluster uses different addresses:

```bash
set ZEEBE_GRPC_ADDRESS=http://localhost:26500
set ZEEBE_REST_ADDRESS=http://localhost:8080
java -jar target\mathWorker.jar
```

The application logs each worker registration on startup and keeps running until interrupted
(Ctrl+C), at which point Spring Boot shuts the client down and drains in-flight jobs.

## Trying it out

Deploy `src/test/resources/mathWorker.bpmn` (and its linked `review-results.form`) to a running
cluster, then start an instance — optionally with `a` and `b` variables, otherwise they default to
`10` and `5` inside each service task. The process runs addition, multiplication, and division in
sequence (each overwriting `result` with its own outcome) and stops at the "Review results" user
task. If `b = 0`, the division task raises a `DIVISION_BY_ZERO` BPMN error instead of completing.
