package fixtures.bank.commandmodel;

import fixtures.bank.command.CreateBankAccountCommand;
import fixtures.bank.command.DepositMoneyCommand;
import fixtures.bank.command.WithdrawMoneyCommand;
import fixtures.bank.commandmodel.exception.InsufficientBalanceException;
import fixtures.bank.commandmodel.exception.MaximalBalanceExceededException;
import fixtures.bank.commandmodel.exception.MaximumActiveMoneyTransfersReachedException;
import fixtures.bank.event.BalanceChangedEvent;
import fixtures.bank.event.BankAccountCreatedEvent;
import fixtures.bank.event.MoneyDepositedEvent;
import fixtures.bank.event.MoneyWithdrawnEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Collections;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(
  snapshotTriggerDefinition = BankAccountAggregate.SNAPSHOT_TRIGGER
  // ,  cache = BankAccountAggregate.CACHE
  )
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
public class BankAccountAggregate {

  public static final String SNAPSHOT_TRIGGER = "bankAccountAggregateSnapshotTriggerDefinition";
  public static final String CACHE = "bankAccountAggregateCache";

  public enum Configuration {
    ;

    public static final int DEFAULT_INITIAL_BALANCE = 0;
    // for this example we assume that there is a maximum of money allowed in your account, so the transfer saga can fail.
    public static final int DEFAULT_MAXIMAL_BALANCE = 1000;
    public static final int MAXIMUM_NUMBER_OF_ACTIVE_MONEY_TRANSFERS = 1;

  }

  /**
   * The accountId.
   */
  @AggregateIdentifier
  private String accountId;

  /**
   * Current state ... how much money does the account effectively hold right now.
   */
  private Integer currentBalance;

  /**
   * Fictual upper bound of the balance,introduced to raise {@link MaximalBalanceExceededException} for testing simplicity.
   */
  private Integer maximalBalance;

  /**
   * <code>null</code> unless a money transfer is currently active.
   */
  private ActiveMoneyTransfer activeMoneyTransfer;

  @CommandHandler
  public static BankAccountAggregate handle(CreateBankAccountCommand cmd) {
    apply(
      BankAccountCreatedEvent
        .newBuilder()
        .setAccountId(cmd.getAccountId())
        .setInitialBalance(of(cmd.getInitialBalance()).orElse(Configuration.DEFAULT_INITIAL_BALANCE))
        .setMaximalBalance(ofNullable(cmd.getMaximalBalance()).orElse(Configuration.DEFAULT_MAXIMAL_BALANCE))
        .build());

    return new BankAccountAggregate();
  }

  @CommandHandler
  void handle(WithdrawMoneyCommand cmd) {
    checkForInsufficientBalance(cmd.getAmount());
    apply(
      MoneyWithdrawnEvent
        .newBuilder()
        .setAccountId(accountId)
        .setAmount(cmd.getAmount())
        .build()
    );
  }

  @CommandHandler
  void handle(DepositMoneyCommand cmd) {
    checkForMaximalBalanceExceeded(cmd.getAmount());

    apply(
      MoneyDepositedEvent
        .newBuilder()
        .setAccountId(accountId)
        .setAmount(cmd.getAmount())
        .build()
    );
  }

  @EventSourcingHandler
  void on(BankAccountCreatedEvent evt) {
    log.info("replaying event: {}", evt);
    this.accountId = evt.getAccountId();
    this.currentBalance = evt.getInitialBalance();
    this.maximalBalance = evt.getMaximalBalance();
  }

  @EventSourcingHandler
  void on(MoneyWithdrawnEvent evt) {
    log.info("replaying event: {}", evt);
    decreaseCurrentBalance(evt.getAmount());
  }

  @EventSourcingHandler
  void on(MoneyDepositedEvent evt) {
    log.info("replaying event: {}", evt);
    increaseCurrentBalance(evt.getAmount());
  }

  //  @CommandHandler
  //  void handle(InitializeMoneyTransferCommand cmd) {
  //    checkForActiveMoneyTransfer();
  //    checkForInsufficientBalance(cmd.getAmount());
  //
  //    apply(MoneyTransferInitializedEvent.builder()
  //      .transactionId(cmd.getTransactionId())
  //      .sourceAccountId(cmd.getSourceAccountId())
  //      .targetAccountId(cmd.getTargetAccountId())
  //      .amount(cmd.getAmount())
  //      .build());
  //  }
  //
  //  @EventSourcingHandler
  //  void on(MoneyTransferInitializedEvent evt) {
  //    log.info("replaying event: {}", evt);
  //    this.activeMoneyTransfer = ActiveMoneyTransfer.builder()
  //      .transactionId(evt.getTransactionId())
  //      .amount(evt.getAmount())
  //      .build();
  //  }
  //
  //  @CommandHandler
  //  void handle(ReceiveMoneyTransferCommand cmd) {
  //    checkForMaximalBalanceExceeded(cmd.getAmount());
  //
  //    apply(MoneyTransferReceivedEvent.builder()
  //      .sourceAccountId(cmd.getSourceAccountId())
  //      .targetAccountId(cmd.getTargetAccountId())
  //      .transactionId(cmd.getTransactionId())
  //      .amount(cmd.getAmount())
  //      .build()
  //    );
  //  }
  //
  //  @EventSourcingHandler
  //  void on(MoneyTransferReceivedEvent evt) {
  //    log.info("replaying event: {}", evt);
  //    increaseCurrentBalance(evt.getAmount());
  //  }
  //
  //  @CommandHandler
  //  void handle(CompleteMoneyTransferCommand cmd) {
  //    if (activeMoneyTransfer == null || !activeMoneyTransfer.transactionId.equals(cmd.getTransactionId())) {
  //      throw new IllegalStateException("not participating in transaction: " + cmd.getTransactionId());
  //    }
  //
  //    apply(MoneyTransferCompletedEvent.builder()
  //      .sourceAccountId(cmd.getSourceAccountId())
  //      .transactionId(cmd.getTransactionId())
  //      .amount(cmd.getAmount())
  //      .build()
  //    );
  //  }
  //
  //  @EventSourcingHandler
  //  void on(MoneyTransferCompletedEvent evt) {
  //    decreaseCurrentBalance(evt.getAmount());
  //    activeMoneyTransfer = null;
  //  }
  //
  //  @CommandHandler
  //  void handle(RollBackMoneyTransferCommand cmd) {
  //    if (activeMoneyTransfer == null || !activeMoneyTransfer.transactionId.equals(cmd.getTransactionId())) {
  //      throw new IllegalStateException("not participating in transaction: " + cmd.getTransactionId());
  //    }
  //
  //    apply(MoneyTransferRolledBackEvent.builder()
  //      .sourceAccountId(cmd.getSourceAccountId())
  //      .transactionId(cmd.getTransactionId())
  //      .build()
  //    );
  //  }
  //
  //  @EventSourcingHandler
  //  void on(MoneyTransferRolledBackEvent evt) {
  //    activeMoneyTransfer = null;
  //  }

  /**
   * @return stored current balance minus amount(s) reserved by active money transfers
   */
  public int getEffectiveCurrentBalance() {
    return currentBalance - ofNullable(activeMoneyTransfer).map(ActiveMoneyTransfer::getAmount).orElse(0);
  }

  private void increaseCurrentBalance(int amount) {
    this.currentBalance += amount;
    apply(BalanceChangedEvent.newBuilder()
                             .setAccountId(accountId)
                             .setNewBalance(this.currentBalance)
                             .build());
  }

  private void decreaseCurrentBalance(int amount) {
    increaseCurrentBalance(-amount);
  }

  private void checkForMaximalBalanceExceeded(int amount) {
    if (maximalBalance < currentBalance + amount) {
      throw new MaximalBalanceExceededException(currentBalance, amount, maximalBalance);
    }
  }

  private void checkForActiveMoneyTransfer() {
    if (activeMoneyTransfer != null) {
      throw new MaximumActiveMoneyTransfersReachedException(
        Configuration.MAXIMUM_NUMBER_OF_ACTIVE_MONEY_TRANSFERS,
        Collections.singletonList(activeMoneyTransfer.transactionId));
    }
  }

  private void checkForInsufficientBalance(int amount) {
    if (getEffectiveCurrentBalance() < amount) {
      throw new InsufficientBalanceException(getEffectiveCurrentBalance(), amount);
    }
  }

  @Value
  @Builder
  public static class ActiveMoneyTransfer {

    @NonNull
    String transactionId;

    int amount;
  }
}
