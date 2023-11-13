package {{packageName}}.application.port.out.message;

import {{packageName}}.domain.PersistentEvent;

public interface MessageProducer {

  boolean produce(PersistentEvent persistentEvent);
}
