package {{packageName}}.application.port.out.persist;

import {{packageName}}.domain.PersistentEvent;
import {{packageName}}.domain.PersistentEvent.PersistentEventStatus;
import java.time.Instant;
import java.util.List;

public interface PersistentEventRepository {

  PersistentEvent save(PersistentEvent persistentEvent);

  List<PersistentEvent> findAllByStatusAndCreatedAfter(PersistentEventStatus status, Instant timeScope);
}
