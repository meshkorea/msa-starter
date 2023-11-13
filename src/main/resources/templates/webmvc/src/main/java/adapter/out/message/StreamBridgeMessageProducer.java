package {{packageName}}.adapter.out.message;

import {{packageName}}.application.port.out.message.MessageProducer;
import {{packageName}}.config.Constants.MessageKey;
import {{packageName}}.domain.PersistentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static {{packageName}}.config.Constants.MessagePolicy.PRODUCER_CHANNEL;
import static {{packageName}}.config.Constants.PROJECT_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class StreamBridgeMessageProducer implements MessageProducer {

  private final StreamBridge streamBridge;

  @Override
  public boolean produce(PersistentEvent persistentEvent) {
    String body = persistentEvent.getBody();
    Message<?> message = MessageBuilder
        .withPayload(body)
        .setHeader(MessageKey.ID, persistentEvent.getEventId())
        .setHeader(MessageKey.TYPE, persistentEvent.getEventType())
        .setHeader(MessageKey.VERSION, 1)
        .setHeader(MessageKey.SOURCE, PROJECT_NAME)
        .setHeader(MessageKey.RESOURCE, body.getClass().getSimpleName())
        .setHeader(MessageKey.PARTITION_KEY, persistentEvent.getPartitionKey())
        .build();

    return streamBridge.send(PRODUCER_CHANNEL, message);
  }
}
