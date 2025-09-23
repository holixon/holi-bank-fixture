package fixtures.bank.query;

import fixtures.bank.query.FindAllBankAccountCurrentBalances.AllBankAccountCurrentBalancesQuery;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.Value;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

public interface FindAllBankAccountCurrentBalances extends
  Function<AllBankAccountCurrentBalancesQuery, CompletableFuture<List<BankAccountCurrentBalanceDto>>> {

  ResponseType<List<BankAccountCurrentBalanceDto>> RESPONSE_TYPE = ResponseTypes.multipleInstancesOf(BankAccountCurrentBalanceDto.class);

  static FindAllBankAccountCurrentBalances create(final QueryGateway queryGateway) {
    return query -> queryGateway.query(query, RESPONSE_TYPE);
  }

  default CompletableFuture<List<BankAccountCurrentBalanceDto>> apply() {
    return apply(AllBankAccountCurrentBalancesQuery.INSTANCE);
  }

  @Value
  class AllBankAccountCurrentBalancesQuery {
    public static final AllBankAccountCurrentBalancesQuery INSTANCE = new AllBankAccountCurrentBalancesQuery();
  }
}
