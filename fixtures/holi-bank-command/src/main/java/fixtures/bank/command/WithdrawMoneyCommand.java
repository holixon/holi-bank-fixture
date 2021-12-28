package fixtures.bank.command;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.serialization.Revision;

/**
 * You are at an ATM, you withdraw money from your account.
 */
@Value
@Builder(toBuilder = true)
@Revision("1")
public class WithdrawMoneyCommand  implements HoliBankCommand{

  @NonNull
  String accountId;

  @NonNull
  Integer amount;
}
