package fixtures.bank.example.spring.rest;

import fixtures.bank.command.CreateBankAccountCommand;
import fixtures.bank.command.DepositMoneyCommand;
import fixtures.bank.command.WithdrawMoneyCommand;
import fixtures.bank.domain.BankAccountAggregate;
import fixtures.bank.domain.BankAccountAggregate.Configuration;
import fixtures.bank.query.BankAccountCurrentBalanceDto;
import fixtures.bank.query.FindAllBankAccountCurrentBalances;
import fixtures.bank.query.FindBankAccountCurrentBalanceByAccountId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HoliBankController {





  private final CommandGateway commandGateway;
  private final FindBankAccountCurrentBalanceByAccountId findByAccountId;
  private final FindAllBankAccountCurrentBalances findAll;

  public HoliBankController(final CommandGateway commandGateway, final QueryGateway queryGateway) {
    this.commandGateway = commandGateway;
    this.findByAccountId = FindBankAccountCurrentBalanceByAccountId.create(queryGateway);
    this.findAll = FindAllBankAccountCurrentBalances.create(queryGateway);
  }

  @PostMapping("/bankaccounts/{bankAccountId}")
  public ResponseEntity<Void> createBankAccount(
    @PathVariable("bankAccountId") String bankAccountId,
    @RequestParam(value = "initialBalance", required = false) Integer initialBalance
  ) {
    commandGateway.sendAndWait(CreateBankAccountCommand.builder()
      .accountId(bankAccountId)
      .initialBalance(Optional.ofNullable(initialBalance).orElse(Configuration.DEFAULT_INITIAL_BALANCE))
      .build());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/bankaccounts/{bankAccountId}")
  public ResponseEntity<Void> update(
    @PathVariable("bankAccountId") String bankAccountId,
    @RequestParam(value = "amount") int amount
  ) {
    if (amount > 0) {
      commandGateway.sendAndWait(DepositMoneyCommand.builder()
        .accountId(bankAccountId)
        .amount(amount)
        .build());
    } else {
      commandGateway.sendAndWait(WithdrawMoneyCommand.builder()
        .accountId(bankAccountId)
        .amount(-amount)
        .build());
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/bankaccounts/{bankAccountId}")
  public ResponseEntity<BankAccountCurrentBalanceDto> findById(
    @PathVariable("bankAccountId") String bankAccountId
  ) {
    return ResponseEntity.of(findByAccountId.apply(bankAccountId).join());
  }

  @GetMapping("/bankaccounts")
  public ResponseEntity<List<BankAccountCurrentBalanceDto>> findAll() {
    return ResponseEntity.ok(findAll.apply().join());
  }
}
