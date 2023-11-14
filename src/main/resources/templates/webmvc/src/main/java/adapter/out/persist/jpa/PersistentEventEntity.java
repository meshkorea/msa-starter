package {{packageName}}.adapter.out.persist.jpa;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "persistent_events")
@DynamicUpdate
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class PersistentEventEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private UUID eventId;

  private String eventType;

  // Use externalId of body
  private UUID partitionKey;

  // TODO create and interface and use that type. e.g. interface PersistentEventBody { UUID getExternalId(); }
  // TODO define @Converter to serialize the body to JSON
//  @Lob
  private String body;

  private Instant createdAt;

  private Instant producedAt;

  private PersistentEventStatus status;

  @Builder
  public PersistentEventEntity(UUID eventId, String eventType, UUID partitionKey, String body, Instant createdAt,
                               Instant producedAt, PersistentEventStatus status) {
    this.eventId = eventId;
    this.eventType = eventType;
    this.partitionKey = partitionKey;
    this.body = body;
    this.createdAt = createdAt;
    this.producedAt = producedAt;
    this.status = status;
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
