import java.time.Instant;

public class DeviceData {
    private String device;
    private String newStatus;
    private String ipv4Adress;
    private Instant time;

    public DeviceData(String device, String newStatus, String ipv4Adress, Instant time) {
        this.device = device;
        this.newStatus = newStatus;
        this.ipv4Adress = ipv4Adress;
        this.time = time;
    }

    @Override
    public String toString(){
        return device
                + ": ip: " + ipv4Adress
                + " status: " + newStatus
                + " time: " + time.toString();
    }
}
