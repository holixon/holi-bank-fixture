package fixtures.bank.command.transfer;

import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.serialization.Revision;

/**
 * Starts a money transfer from sourceAccountId to targetAccountId.
 */
@Value
@Builder
@Revision("1")
public class InitializeMoneyTransferCommand implements HoliBankTransferCommand {

  @NonNull
  String sourceAccountId;

  @NonNull
  String targetAccountId;

  @Builder.Default
  String transactionId = UUID.randomUUID().toString();

  int amount;
}
