import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DatabaseWork  {

    private Statement statement = null;

    private HashMap getProperties() throws IOException {
        HashMap<String, String> params = new HashMap<>();
        FileInputStream fis;
        Properties property = new Properties();
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            params.put("host", property.getProperty("db.host"));
            params.put("login", property.getProperty("db.login"));
            params.put("password", property.getProperty("db.password"));
            params.put("dbname", property.getProperty("db.dbname"));
            property.clear();

            return params;
    }

    private Statement baseConnection() throws SQLException, IOException {
        var baseParams = getProperties();
        String url = "jdbc:postgresql://" + baseParams.get("host") + "/" + baseParams.get("dbname");
        Connection conn = DriverManager.getConnection(url, (String)baseParams.get("login"), (String)baseParams.get("password"));
        return conn.createStatement();
    }

    public String getBalance(int userId) throws SQLException, IOException {
        statement = baseConnection();
        ResultSet resultSet;
        double balance = -1;
        ObjectMapper objectMapper = new ObjectMapper();

        if (statement != null) {
            String sql = "select balance from balans where id=" + userId;
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
            resultSet.close();
            statement.close();
        }

        if (balance == -1) {
            Error error = new Error("Пользователь не найден");
            return objectMapper.writeValueAsString(error);
        } else {
            Balance balanceJson = new Balance(userId, balance);
            return objectMapper.writeValueAsString(balanceJson);
        }
    }

    public String takeMoney(int userId, double sum) throws SQLException, IOException {
        double balance;
        JsonNode rootNode;
        ObjectMapper objectMapper = new ObjectMapper();

        if (sum < 0 || sum == 0) {
            Error error = new Error("Неправильно введена сумма списывания");
            return objectMapper.writeValueAsString(error);
        }

        try {
            rootNode = objectMapper.readValue(getBalance(userId), JsonNode.class);
            balance = rootNode.get("balance").asDouble();
        } catch (NullPointerException e) {
            Error error = new Error("Ошибка получения баланса");
            return objectMapper.writeValueAsString(error);
        }

        if (balance < sum) {
            Error error = new Error("Недостаточно средств");
            return objectMapper.writeValueAsString(error);
        }
        statement = baseConnection();
        if (statement != null) {
            String sql = "update balans set balance=balance-" + sum + "where id=" + userId;
            statement.executeUpdate(sql);
        }

        statement.close();
        Answer answer = new Answer("Успешно");
        return objectMapper.writeValueAsString(answer);
    }

    public String putMoney(int userId, double sum) throws SQLException, IOException {
        statement = baseConnection();
        ObjectMapper objectMapper = new ObjectMapper();

        if (sum < 0 || sum == 0) {
            Error error = new Error("Неправильно введена сумма списывания");
            return objectMapper.writeValueAsString(error);
        }

        if (statement != null) {
            String sql = "update balans set balance=balance+" + sum + "where id=" + userId;
            statement.executeUpdate(sql);
        }

        statement.close();
        Answer answer = new Answer("Успешно");
        return objectMapper.writeValueAsString(answer);
    }
}
