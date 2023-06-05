package model;

import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class TransactionDAO {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * 계좌 이체 : 출금 계좌, 입금 계좌, 출금 금액
     */
    public void transfer(Transaction transaction) {
        String query = "insert into transaction_tb (transaction_amount, transaction_w_balance, transaction_d_balance, transaction_w_account_number, transaction_d_account_number, transaction_created_at) values (?, ?, ?, ?, ?, now())";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, transaction.getTransactionAmount());
            statement.setInt(2, transaction.getTransactionWBalance());
            statement.setInt(3, transaction.getTransactionDBalance());
            statement.setInt(4, transaction.getTransactionWAccountNumber());
            statement.setInt(5, transaction.getTransactionDAccountNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
