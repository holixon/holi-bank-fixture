package fixtures.bank.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BankAccountCurrentBalanceDto {

  String accountId;

  int currentBalance;

}
