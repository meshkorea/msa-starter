package {{packageName}}.adapter.in.message;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer implements Consumer<Message<?>> {

  @Override
  public void accept(Message<?> message) {
    log.info("A message received: {}", new String((byte[]) message.getPayload()));
    // Add business logic here
  }
}
