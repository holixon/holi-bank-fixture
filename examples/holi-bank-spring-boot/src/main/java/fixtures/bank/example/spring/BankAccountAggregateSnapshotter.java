package fixtures.bank.example.spring;

import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.event.BankAccountSnapshotEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;


@Slf4j
public class BankAccountAggregateSnapshotter extends AggregateSnapshotter {

  public BankAccountAggregateSnapshotter(Builder builder) {
    super(builder);
  }

  @Override
  protected DomainEventMessage createSnapshot(Class<?> aggregateType, String aggregateIdentifier, DomainEventStream eventStream) {
    log.info("Creating snapshot for {} {}", aggregateType.getSimpleName(), aggregateIdentifier);
    DomainEventMessage aggregateSnapshotEvent = super.createSnapshot(aggregateType, aggregateIdentifier, eventStream);
    if (aggregateSnapshotEvent != null) {
      BankAccountAggregate aggregate = (BankAccountAggregate) aggregateSnapshotEvent.getPayload();
      BankAccountSnapshotEvent event = new BankAccountSnapshotEvent(aggregate.getAccountId(), aggregate.getCurrentBalance(), aggregate.getMaximalBalance());
      return new GenericDomainEventMessage(aggregateSnapshotEvent.getType(), aggregateSnapshotEvent.getAggregateIdentifier(), aggregateSnapshotEvent.getSequenceNumber(), event);
    }
    return null;
  }
}
