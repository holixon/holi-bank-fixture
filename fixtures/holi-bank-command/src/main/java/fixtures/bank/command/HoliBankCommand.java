package fixtures.bank.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public interface HoliBankCommand {

  @TargetAggregateIdentifier
  String getAccountId();

}
