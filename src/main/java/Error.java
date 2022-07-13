import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    @JsonProperty("error")
    private String message;

    public Error(String message) {
        this.message = message;
    }
}
