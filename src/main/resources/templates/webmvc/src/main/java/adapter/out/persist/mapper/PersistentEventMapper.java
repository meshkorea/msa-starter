package {{packageName}}.adapter.out.persist.mapper;

import {{packageName}}.adapter.out.persist.jpa.PersistentEventEntity;
import {{packageName}}.adapter.out.persist.jpa.PersistentEventEntity.PersistentEventStatus;
import {{packageName}}.domain.PersistentEvent;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PersistentEventMapper {

  public PersistentEventEntity toEntity(PersistentEvent event) {
    return PersistentEventEntity.builder()
        .eventId(event.getEventId())
        .eventType(event.getEventType())
        .partitionKey(event.getPartitionKey())
        .body(event.getBody())
        .createdAt(event.getCreatedAt())
        .producedAt(event.getProducedAt())
        .status(toEntity(event.getStatus()))
        .build();
  }

  public PersistentEventStatus toEntity(PersistentEvent.PersistentEventStatus status) {
    return PersistentEventStatus.valueOf(status.name());
  }

  public PersistentEvent toModel(PersistentEventEntity entity) {
    return new PersistentEvent(entity.getId(),
        entity.getEventId(),
        entity.getEventType(),
        entity.getPartitionKey(),
        entity.getBody(),
        entity.getCreatedAt(),
        entity.getProducedAt(),
        toModel(entity.getStatus())
    );
  }

  public PersistentEvent.PersistentEventStatus toModel(PersistentEventStatus entity) {
    return PersistentEvent.PersistentEventStatus.valueOf(entity.name());
  }

  public List<PersistentEvent> toModels(Collection<PersistentEventEntity> entities) {
    return entities.stream()
        .map(this::toModel)
        .toList();
  }
}
