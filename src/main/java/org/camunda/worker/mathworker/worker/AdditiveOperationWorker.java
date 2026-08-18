package org.camunda.worker.mathworker.worker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Job worker for the "addition" job type: completes the job with {@code result = a + b}. */
@Component
public class AdditiveOperationWorker {

  private static final Logger log = LoggerFactory.getLogger(AdditiveOperationWorker.class);

  @JobWorker(type = "addition", name = "addition")
  public Map<String, Object> addition(@Variable final int a, @Variable final int b) {
    final int result = a + b;
    log.info("addition: {} + {} = {}", a, b, result);
    return Map.of("result", result);
  }

  @JobWorker(type = "substraction", name = "substraction")
  public Map<String, Object> substraction(@Variable final int a, @Variable final int b) {
    final int result = a - b;
    log.info("substraction: {} + {} = {}", a, b, result);
    return Map.of("result", result);
  }
}
