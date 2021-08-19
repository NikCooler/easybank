package org.craftedsw.type;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.craftedsw.type.StatementType.CREDIT;
import static org.craftedsw.type.StatementType.DEBIT;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatement {

    private static final String PRINT_FORMULA = "| %20s | %12s | %12s | %12s |";
    private static final String HEADER = format(PRINT_FORMULA, "date", "credit", "debit", "balance");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    private final List<TransactionStatement> transactionStatements = new ArrayList<>();

    public void addTransactionStatement(TransactionStatement transactionStatement) {
        transactionStatements.add(transactionStatement);
    }

    public List<String> buildStatements() {
        var statements = new ArrayList<String>();
        statements.add(HEADER);

        transactionStatements.forEach(ts -> {
            var act = ts.getAct();
            var debit = ts.getType() == DEBIT ? act.getChange() : null;
            var credit = ts.getType() == CREDIT ? act.getChange() : null;
            var st = format(PRINT_FORMULA,
                    DATE_FORMATTER.format(ts.getDate()),
                    formatAmount(credit),
                    formatAmount(debit),
                    formatAmount(act.getTotal())
            );
            statements.add(st);
        });

        return statements;
    }

    private String formatAmount(Amount amount) {
        return ofNullable(amount)
                .map(a -> format("%s %s", a.getValue(), a.getCurrency().getTitle()))
                .orElse("");
    }
}
