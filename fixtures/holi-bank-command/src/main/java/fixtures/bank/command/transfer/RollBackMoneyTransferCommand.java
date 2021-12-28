package fixtures.bank.command.transfer;

import fixtures.bank.command.HoliBankCommand;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

@Value
@Builder(toBuilder = true)
@Revision("1")
public class RollBackMoneyTransferCommand implements HoliBankCommand {

  @NonNull
  String sourceAccountId;

  @NonNull
  String transactionId;

  @Override
  public String getAccountId() {
    return getSourceAccountId();
  }
}
