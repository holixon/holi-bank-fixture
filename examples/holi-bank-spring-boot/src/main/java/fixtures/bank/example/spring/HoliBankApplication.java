package fixtures.bank.example.spring;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.example.spring.rest.HoliBankController;
import fixtures.bank.projection.BankAccountCurrentBalanceProjection;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
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
    Snapshotter snapshotter,
    @Value("${axon.aggregate.bank-account.snapshot-threshold:10}") int threshold) {
    return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
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
