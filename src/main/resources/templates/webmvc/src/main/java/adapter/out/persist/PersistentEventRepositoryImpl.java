package {{packageName}}.adapter.out.persist;

import {{packageName}}.adapter.out.persist.jpa.PersistentEventEntity;
import {{packageName}}.adapter.out.persist.jpa.repository.PersistentEventJpaRepository;
import {{packageName}}.adapter.out.persist.mapper.PersistentEventMapper;
import {{packageName}}.application.port.out.persist.PersistentEventRepository;
import {{packageName}}.domain.PersistentEvent;
import {{packageName}}.domain.PersistentEvent.PersistentEventStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistentEventRepositoryImpl implements PersistentEventRepository {

  private final PersistentEventJpaRepository repository;
  private final PersistentEventMapper mapper;

  @Override
  public PersistentEvent save(PersistentEvent persistentEvent) {
    PersistentEventEntity saved = repository.save(mapper.toEntity(persistentEvent));

    return mapper.toModel(saved);
  }

  @Override
  public List<PersistentEvent> findAllByStatusAndCreatedAfter(PersistentEventStatus status, Instant createdAt) {
    List<PersistentEventEntity> entities = repository.findAllByStatusIsAndCreatedAtAfter(mapper.toEntity(status), createdAt);

    return mapper.toModels(entities);
  }
}
