package fixtures.bank.example.spring;

import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.projection.BankAccountCurrentBalanceProjection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableWebMvc
@ComponentScan(basePackageClasses = {BankAccountAggregate.class})
public class HoliBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoliBankApplication.class, args);
  }

  @Bean
  public BankAccountCurrentBalanceProjection bankAccountCurrentBalanceProjection() {
    return new BankAccountCurrentBalanceProjection();
  }
}
