package org.craftedsw.dto;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatementDto {

    private final List<String> statements;

    public UserAccountsStatementDto(List<String> statements) {
        this.statements = statements;
    }

    public List<String> getStatements() {
        return statements;
    }
}
