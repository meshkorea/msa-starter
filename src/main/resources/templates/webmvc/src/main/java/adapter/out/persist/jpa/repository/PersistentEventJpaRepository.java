package {{packageName}}.adapter.out.persist.jpa.repository;

import {{packageName}}.adapter.out.persist.jpa.PersistentEventEntity;
import {{packageName}}.adapter.out.persist.jpa.PersistentEventEntity.PersistentEventStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentEventJpaRepository extends JpaRepository<PersistentEventEntity, Long> {

  List<PersistentEventEntity> findAllByStatusIsAndCreatedAtAfter(PersistentEventStatus status, Instant createdAt);
}
