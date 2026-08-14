package org.camunda.worker.mathwoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the mathWorker application: the Camunda Spring Boot Starter opens a CamundaClient
 * against the configured cluster and registers every {@code @JobWorker} bean.
 */
@SpringBootApplication
public class MathWorkerApplication {

  public static void main(final String[] args) {
    SpringApplication.run(MathWorkerApplication.class, args);
  }
}
