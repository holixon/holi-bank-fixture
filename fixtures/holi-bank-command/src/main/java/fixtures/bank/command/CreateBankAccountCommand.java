package fixtures.bank.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class CreateBankAccountCommand implements HoliBankCommand {

  @NonNull
  String accountId;

  @NonNull
  Integer initialBalance;

  Integer maximalBalance;
}
