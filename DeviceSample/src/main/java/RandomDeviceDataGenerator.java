import java.time.Instant;
        import java.util.Random;

public class RandomDeviceDataGenerator {

    private Random rand;
    private final static String[] devices = {
            "Laptop",
            "Telefon",
            "Pralka",
            "Suszarka",
            "Toster"
    };

    private final static String[] statuses = {
            "Aktywacja",
            "Deaktywacja",
            "Czuwanie",
            "Awaria"
    };

    private String randomIpv4(){
        return rand.nextInt(256) +
                "." +
                rand.nextInt(256) +
                "." +
                rand.nextInt(256) +
                "." +
                rand.nextInt(256);
    }

    public RandomDeviceDataGenerator() {
        this.rand = new Random();
    }

    public DeviceData generateRandomDeviceData(){
        return new DeviceData(devices[rand.nextInt(5)],
                statuses[rand.nextInt(4)],
                randomIpv4(),
                Instant.now());
    }
}
