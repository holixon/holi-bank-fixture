package fixtures.bank.query;

import fixtures.bank.query.FindBankAccountCurrentBalanceByAccountId.BankAccountCurrentBalanceByAccountIdQuery;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

public interface FindBankAccountCurrentBalanceByAccountId extends
  Function<BankAccountCurrentBalanceByAccountIdQuery, CompletableFuture<Optional<BankAccountCurrentBalanceDto>>> {

  ResponseType<Optional<BankAccountCurrentBalanceDto>> RESPONSE_TYPE = ResponseTypes.optionalInstanceOf(BankAccountCurrentBalanceDto.class);

  static FindBankAccountCurrentBalanceByAccountId create(final QueryGateway queryGateway) {
    return query -> queryGateway.query(query, RESPONSE_TYPE);
  }

  default CompletableFuture<Optional<BankAccountCurrentBalanceDto>> apply(String accountId) {
    return apply(BankAccountCurrentBalanceByAccountIdQuery.of(accountId));
  }

  @Value
  @Builder
  class BankAccountCurrentBalanceByAccountIdQuery {

    @NonNull
    String accountId;

    public static BankAccountCurrentBalanceByAccountIdQuery of(String accountId) {
      return BankAccountCurrentBalanceByAccountIdQuery.builder().accountId(accountId).build();
    }
  }
}
