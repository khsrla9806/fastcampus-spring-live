package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private Connection connection;

    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * SELECT : 전체 계좌 조회
     */
    public List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();

        // 1. SQL
        String query = "select * from account_tb";

        try {
            // 2. 버퍼에 담기
            PreparedStatement statement = connection.prepareStatement(query);

            // 3. DBMS 전송
            ResultSet rs = statement.executeQuery();

            // 3.1 cursor 내리기
            while (rs.next()) {
                // 3.2 projection 하기
                Account account = new Account(
                        rs.getInt("account_number"),
                        rs.getString("account_password"),
                        rs.getInt("account_balance"),
                        rs.getTimestamp("account_created_at")
                );
                accountList.add(account);
            }
            // 4. mapping (매핑, result -> model)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accountList;
    }

    /**
     * SELECT : 계좌 조회 (1건)
     */
    public Account getAccountNumber(int accountNumber) {
        // 1. SQL
        String query = "select * from account_tb where account_number = ?";

        try {
            // 2. 버퍼에 담기
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountNumber);

            // 3. DBMS 전송
            ResultSet rs = statement.executeQuery();

            // 3.1 cursor 내리기
            if (rs.next()) {
                // 3.2 projection 하기
                Account account = new Account(
                        rs.getInt("account_number"),
                        rs.getString("account_password"),
                        rs.getInt("account_balance"),
                        rs.getTimestamp("account_created_at")
                );

                return account;
            }
            // 4. mapping (매핑, result -> model)
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    /**
     * INSERT 쿼리
     */
    public void createAccount(int accountNumber, String accountPassword) {
        // 1. SQL 생성
        String query = "insert into account_tb values(?, ?, 1000, now())";

        try {
            // 2. Buffer 생성
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountNumber);
            statement.setString(2, accountPassword);

            // 3. DBMS 전송
            int result = statement.executeUpdate();
            System.out.println("result = " + result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE 쿼리
     */
    public void updateAccount(int accountBalance, int accountNumber) {
        // 1. SQL 생성
        String query = "update account_tb set account_balance = ? where account_number = ?";

        try {
            // 2. Buffer 생성
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountBalance);
            statement.setInt(2, accountNumber);

            // 3. DBMS 전송
            int result = statement.executeUpdate();
            System.out.println("result = " + result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * DELETE 쿼리
     */
    public void deleteAccount(int accountNumber) {
        // 1. SQL 생성
        String query = "delete from account_tb where account_number = ?";

        try {
            // 2. Buffer 생성
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, accountNumber);

            // 3. DBMS 전송
            int result = statement.executeUpdate();
            System.out.println("result = " + result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
