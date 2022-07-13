package fixtures.bank.example.spring;

import fixtures.bank.commandmodel.BankAccountAggregate;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankAggregateTuningConfiguration {

  @Bean(name = BankAccountAggregate.SNAPSHOT_TRIGGER)
  public SnapshotTriggerDefinition bankAccountAggregateSnapshotTriggerDefinition(
    @Value("${axon.aggregate.bank-account.snapshot-threshold:10}") int threshold, Snapshotter snapshotter) {
    return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
  }

  @Bean(name = BankAccountAggregate.CACHE)
  public Cache bankAccountAggregateCache() {
    return new WeakReferenceCache();
  }

}
