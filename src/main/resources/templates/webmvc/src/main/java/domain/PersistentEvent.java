/**
 * For DomainEvent spec: @see https://wiki.mm.meshkorea.net/display/MES/Event+Schema
 * To secure at-least-once publishing: @see https://wiki.mm.meshkorea.net/pages/viewpage.action?pageId=62645748
 */
package {{packageName}}.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersistentEvent {

  private Long id;

  private UUID eventId;

  private String eventType;

  private UUID partitionKey;

  private String body;

  private Instant createdAt;

  private Instant producedAt;

  private PersistentEventStatus status = PersistentEventStatus.CREATED;

  public static PersistentEvent newInstance(String eventType, UUID partitionKey, String body) {
    return new PersistentEvent(eventType, partitionKey, body);
  }

  private PersistentEvent(String eventType, UUID partitionKey, String body) {
    this.eventType = eventType;
    this.partitionKey = partitionKey;
    this.body = body;
  }

  public void markProduced() {
    this.status = PersistentEventStatus.PRODUCED;
    this.producedAt = Instant.now();
  }

  public void markFailed() {
    this.status = PersistentEventStatus.FAILED;
  }

  @Getter
  @AllArgsConstructor
  public enum PersistentEventStatus {

    CREATED(10),
    PRODUCED(20),
    CONSUMED(30),
    FAILED(40);

    private final Integer code;
  }
}
