package fixtures.bank.example.spring;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import fixtures.bank.commandmodel.BankAccountAggregate;
import fixtures.bank.projection.BankAccountCurrentBalanceProjection;
import fixtures.bank.rest.HoliBankRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
  BankAccountAggregate.class,
})
@Import({
  BankAggregateTuningConfiguration.class,
  HoliBankRestConfiguration.class
})
public class HoliBankJpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoliBankJpaApplication.class, args);
  }

  @Bean
  public BankAccountCurrentBalanceProjection bankAccountCurrentBalanceProjection() {
    return new BankAccountCurrentBalanceProjection();
  }

  @Bean
  public XStream unsecuredXStream() {
    XStream xstream = new XStream();
    xstream.addPermission(AnyTypePermission.ANY);
    return xstream;
  }

}
