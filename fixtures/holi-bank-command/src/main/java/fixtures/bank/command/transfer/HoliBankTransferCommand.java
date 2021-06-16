package fixtures.bank.command.transfer;

import fixtures.bank.command.HoliBankCommand;

public interface HoliBankTransferCommand extends HoliBankCommand {

  String getSourceAccountId();

  String getTargetAccountId();

  String getTransactionId();

  int getAmount();

  @Override
  default String getAccountId() {
    return getSourceAccountId();
  }
}
