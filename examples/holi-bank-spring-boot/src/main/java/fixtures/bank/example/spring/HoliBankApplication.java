package fixtures.bank.example.spring;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.example.spring.rest.HoliBankController;
import fixtures.bank.projection.BankAccountCurrentBalanceProjection;
import org.axonframework.common.DirectExecutor;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.config.Configuration;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.HandlerDefinition;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.modelling.command.RepositoryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
  BankAccountAggregate.class, HoliBankController.class
})
public class HoliBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoliBankApplication.class, args);
  }

  @Bean
  public BankAccountCurrentBalanceProjection bankAccountCurrentBalanceProjection() {
    return new BankAccountCurrentBalanceProjection();
  }

  @Bean(name = BankAccountAggregate.SNAPSHOT_TRIGGER)
  public SnapshotTriggerDefinition bankAccountAggregateSnapshotTriggerDefinition(
    @Value("${axon.aggregate.bank-account.snapshot-threshold:10}") int threshold,
    EventStore eventStore,
    ParameterResolverFactory parameterResolverFactory,
    RepositoryProvider repositoryProvider,
    HandlerDefinition handlerDefinition,
    AggregateFactory<BankAccountAggregate> factory
  ) {
    BankAccountAggregateSnapshotter snapshotter = new BankAccountAggregateSnapshotter(
      AggregateSnapshotter
        .builder()
        .eventStore(eventStore)
        .executor(DirectExecutor.INSTANCE)
        .transactionManager(NoTransactionManager.INSTANCE)
        .aggregateFactories(factory)
        .parameterResolverFactory(parameterResolverFactory)
        .repositoryProvider(repositoryProvider)
        .handlerDefinition(handlerDefinition)
    );
    return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
  }

  @Bean
  public RepositoryProvider repositoryProvider(Configuration configuration) {
      return configuration::repository;
  }

  @Bean(name = BankAccountAggregate.CACHE)
  public Cache giftCardCache() {
    return new WeakReferenceCache();
  }

  @Bean
  public XStream unsecuredXStream() {
    XStream xstream = new XStream();
    xstream.addPermission(AnyTypePermission.ANY);
    return xstream;
  }

}
