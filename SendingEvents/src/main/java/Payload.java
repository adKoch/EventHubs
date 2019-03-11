import java.time.Instant;

public class Payload {
    private Instant time;
    private Integer number;
    private String from;

    Payload(String from, Instant time, Integer number) {
        this.from=from;
        this.time = time;
        this.number = number;
    }

    @Override
    public String toString(){
        return "Payload: from: "+ from +" time: "+ time.toString() +" num: "+number;
    }
}
