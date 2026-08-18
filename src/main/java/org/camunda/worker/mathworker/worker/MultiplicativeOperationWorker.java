package org.camunda.worker.mathworker.worker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import java.util.Map;

import io.camunda.client.exception.CamundaError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Job worker for the "multiplication" job type: completes the job with {@code result = a * b}. */
@Component
public class MultiplicativeOperationWorker {

  private static final Logger log = LoggerFactory.getLogger(MultiplicativeOperationWorker.class);
  public static final String ERROR_DIVISION_BY_ZERO = "DIVISION_BY_ZERO";

  @JobWorker(type = "multiplication", name = "multiplication")
  public Map<String, Object> multiplication(@Variable final int a, @Variable final int b) {
    final int result = a * b;
    log.info("multiplication: {} * {} = {}", a, b, result);
    return Map.of("result", result);
  }

  @JobWorker(type = "division", name = "division")
  public Map<String, Object> division(@Variable final int a, @Variable final int b) {
    if (b == 0) {
      log.warn("division: {} / {} rejected, division by zero", a, b);
      throw CamundaError.bpmnError(ERROR_DIVISION_BY_ZERO, "Division by zero: " + a + " / " + b);
    }

    final int result = a / b;
    log.info("division: {} / {} = {}", a, b, result);
    return Map.of("result", result);
  }
}
