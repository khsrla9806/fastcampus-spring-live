import db.DBConnection;
import model.Account;
import model.AccountDAO;
import model.Transaction;
import model.TransactionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BankApp {
    public static void main(String[] args) {
        Connection connection = DBConnection.getInstance();

        AccountDAO accountDAO = new AccountDAO(connection);

        System.out.println("==== 단일 계좌 조회 ====");
        Account account = accountDAO.getAccountNumber(1111);
        System.out.println(">>> " + account);
        System.out.println();


        System.out.println("==== 전체 계좌 정보 ====");
        List<Account> accounts = accountDAO.getAccountList();
        for (Account a : accounts) {
            System.out.println(">>> " + a);
        }
        System.out.println();

        System.out.println("==== 계좌 이체 ====");
        TransactionDAO transactionDAO = new TransactionDAO(connection);
        String wAccountPassword = "5678";
        int wAccountNumber = 2222;
        int dAccountNumber = 1111;
        int amount = 1000;

        // 0원 이체
        if (amount <= 0) {
            System.out.println("[유효성 오류] 0원 이하의 금액은 이체할 수 없습니다.");
            return;
        }

        // 동일 계좌 이체
        if (wAccountNumber == dAccountNumber) {
            System.out.println("[유효성 오류] 입출금 계좌가 동일할 수 없습니다.");
        }

        // ============== 트랜잭션 시작 =================
        try {
            connection.setAutoCommit(false);

            // 입출금 계좌 찾기
            Account wAccount = accountDAO.getAccountNumber(wAccountNumber);
            Account dAccount = accountDAO.getAccountNumber(dAccountNumber);

            // 존재하는 계좌인지 확인
            if (wAccount == null) {
                throw new RuntimeException("[오류] 출금 계좌가 존재하지 않습니다.");
            }

            if (dAccount == null) {
                throw new RuntimeException("[오류] 입금 계좌가 존재하지 않습니다.");
            }

            // 계좌의 비밀번호 확인
            if (!wAccount.getAccountPassword().equals(wAccountPassword)) {
                throw new RuntimeException("[오류] 출금 계좌의 비밀번호가 올바르지 않습니다.");
            }

            // 출금 계좌 잔액 확인
            if (wAccount.getAccountBalance() < amount) {
                throw new RuntimeException("[오류] 출금 계좌의 잔액이 부족합니다.");
            }

            // 계좌 정보 업데이트
            int wBalance = wAccount.getAccountBalance() - amount;
            int dBalance = dAccount.getAccountBalance() + amount;
            accountDAO.updateAccount(wBalance, wAccount.getAccountNumber());
            accountDAO.updateAccount(dBalance, dAccount.getAccountNumber());

            // 계좌 이체 이력 (트랜잭션) 남기기
            Transaction transaction = new Transaction(
                    amount,
                    wBalance,
                    dBalance,
                    wAccountNumber,
                    dAccountNumber
            );
            transactionDAO.transfer(transaction);
            connection.commit();
            System.out.println("[성공] 계좌 이체를 성공했습니다.");
        } catch (Exception e) {
            try {
                // 원자성을 보장하기 위해서 트랜잭션 내부에 하나라도 실패하면 모두 rollback
                connection.rollback();
                System.out.println("[catch] " + e.getMessage());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } finally {
            try {
                // 계좌 이체 트랜잭션이 끝났으니 다시 auto commit을 true 설정
                connection.setAutoCommit(true);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        System.out.println();
    }
}
