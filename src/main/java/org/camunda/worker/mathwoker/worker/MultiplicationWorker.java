package org.camunda.worker.mathwoker.worker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Job worker for the "multiplication" job type: completes the job with {@code result = a * b}. */
@Component
public class MultiplicationWorker {

  private static final Logger log = LoggerFactory.getLogger(MultiplicationWorker.class);

  @JobWorker(type = "multiplication", name = "mathWorker")
  public Map<String, Object> multiplication(@Variable final int a, @Variable final int b) {
    final int result = a * b;
    log.info("multiplication: {} * {} = {}", a, b, result);
    return Map.of("result", result);
  }
}
