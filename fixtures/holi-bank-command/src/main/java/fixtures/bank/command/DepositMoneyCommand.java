package fixtures.bank.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

/**
 * You are at the bank counter and deposit money to your account.
 */
@Value
@Revision("1")
@Builder(toBuilder = true)
public class DepositMoneyCommand implements HoliBankCommand {

  @NonNull
  String accountId;

  @NonNull
  Integer amount;
}
