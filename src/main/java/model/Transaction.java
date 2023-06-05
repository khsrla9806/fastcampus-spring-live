package model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Transaction {
    private int transactionNumber;
    private int transactionAmount;
    private int transactionWBalance; // 출금 금액
    private int transactionDBalance; // 입금 금액
    private int transactionWAccountNumber; // 출금 계좌
    private int transactionDAccountNumber; // 입금 계좌
    private Timestamp transactionCreatedAt;

    public Transaction(
            int transactionAmount,
            int transactionWBalance,
            int transactionDBalance,
            int transactionWAccountNumber,
            int transactionDAccountNumber
    ) {
        this.transactionAmount = transactionAmount;
        this.transactionWBalance = transactionWBalance;
        this.transactionDBalance = transactionDBalance;
        this.transactionWAccountNumber = transactionWAccountNumber;
        this.transactionDAccountNumber = transactionDAccountNumber;
    }
}
