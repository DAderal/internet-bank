
import java.io.IOException;

import java.sql.SQLException;


public class Main {

        public static void main(String[] args) throws SQLException, IOException {
            DatabaseWork operation = new DatabaseWork();

            System.out.println(operation.getBalance(1));
            System.out.println(operation.takeMoney(1, 10));
            System.out.println(operation.putMoney(1, 10));
    }
}
