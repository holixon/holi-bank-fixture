package fixtures.bank.projection;


import fixtures.bank.event.BankAccountCreatedEvent;
import fixtures.bank.event.MoneyDepositedEvent;
import fixtures.bank.event.MoneyWithdrawnEvent;
import fixtures.bank.query.BankAccountCurrentBalanceDto;
import fixtures.bank.query.FindAllBankAccountCurrentBalances.AllBankAccountCurrentBalancesQuery;
import fixtures.bank.query.FindBankAccountCurrentBalanceByAccountId.BankAccountCurrentBalanceByAccountIdQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

public class BankAccountCurrentBalanceProjection {

  private final Map<String, BankAccountCurrentBalanceDto> store = new ConcurrentHashMap<>();

  @EventHandler
  public void on(BankAccountCreatedEvent evt) {
    store.put(evt.getAccountId(), BankAccountCurrentBalanceDto.builder()
      .accountId(evt.getAccountId())
      .currentBalance(evt.getInitialBalance())
      .build());
  }

  @EventHandler
  public void on(MoneyDepositedEvent evt) {
    update(evt.getAccountId(), evt.getAmount());
  }

  @EventHandler
  public void on(MoneyWithdrawnEvent evt) {
    update(evt.getAccountId(), -evt.getAmount());
  }

//  @EventHandler
//  void on(MoneyTransferReceivedEvent evt) {
//    update(evt.getTargetAccountId(), evt.getAmount());
//  }
//
//  @EventHandler
//  void on(MoneyTransferCompletedEvent evt) {
//    update(evt.getSourceAccountId(), -evt.getAmount());
//  }

  private void update(String accountId, int amount) {
    store.computeIfPresent(accountId, (id, dto) -> dto.toBuilder().currentBalance(dto.getCurrentBalance() + amount).build());
  }

  @QueryHandler
  public Optional<BankAccountCurrentBalanceDto> query(BankAccountCurrentBalanceByAccountIdQuery query) {
    return Optional.ofNullable(store.get(query.getAccountId()));
  }

  @QueryHandler
  public List<BankAccountCurrentBalanceDto> query(AllBankAccountCurrentBalancesQuery query) {
    return new ArrayList<>(store.values());
  }

}
