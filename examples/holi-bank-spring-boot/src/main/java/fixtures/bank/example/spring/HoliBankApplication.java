package fixtures.bank.example.spring;

import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.projection.BankAccountCurrentBalanceProjection;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackageClasses = {BankAccountAggregate.class})
@EnableWebMvc
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
      @Value("${axon.aggregate.bank-account.snapshot-threshold:250}") int threshold) {
      return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
  }
}
