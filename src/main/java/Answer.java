import com.fasterxml.jackson.annotation.JsonProperty;

public class Answer {

    @JsonProperty("answer")
    private String message;

    public Answer(String message) {
        this.message = message;
    }
}
