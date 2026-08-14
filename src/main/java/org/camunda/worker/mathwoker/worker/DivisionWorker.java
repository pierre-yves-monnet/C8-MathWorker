package org.camunda.worker.mathwoker.worker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.camunda.client.exception.CamundaError;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Job worker for the "division" job type: completes the job with {@code result = a / b}.
 *
 * <p>Division by zero is a modelled business outcome, not a transient failure: it is signalled as
 * a BPMN error ({@code DIVISION_BY_ZERO}) so a matching error boundary event can catch it, rather
 * than retried.
 */
@Component
public class DivisionWorker {

  private static final Logger log = LoggerFactory.getLogger(DivisionWorker.class);

  public static final String ERROR_DIVISION_BY_ZERO = "DIVISION_BY_ZERO";

  @JobWorker(type = "division", name = "mathWorker")
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
