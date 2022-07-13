import com.fasterxml.jackson.annotation.JsonProperty;

public class Balance {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("balance")
    private double balance;

    String status;

    public Balance(int userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
