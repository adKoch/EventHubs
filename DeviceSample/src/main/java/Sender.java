import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Sender {

    private EventHubClient ehClient;
    private ScheduledExecutorService executorService;

    Sender(String namespaceName, String eventHubName, String sasKeyName, String sasKey) throws IOException, EventHubException {
        final ConnectionStringBuilder publisherConnectionStringBuilder = new ConnectionStringBuilder()
                .setNamespaceName(namespaceName)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey);

        executorService = Executors.newScheduledThreadPool(4);
        ehClient = EventHubClient.createSync(publisherConnectionStringBuilder.toString(), executorService);
    }

    public void sendRandomDeviceData()
            throws EventHubException{

        final Gson gson = new GsonBuilder().create();
        RandomDeviceDataGenerator dataGenerator = new RandomDeviceDataGenerator();

        final  EventDataBatch events = ehClient.createBatch();
        EventData event;

        for(int i=0;i<1000;i++) {
            final DeviceData payload = dataGenerator.generateRandomDeviceData();
            final byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
            event = EventData.create(payloadBytes);
            //events.tryAdd(event);
            ehClient.sendSync(event);
            System.out.println("Zakończono wysyłanie danych urządzenia: " + payload.toString());
        }

        System.out.println("Zakończono wysyłanie");
    }

    public void close(){
        try {
            ehClient.closeSync();
            System.out.println("Zakończono pracę klienta");
        } catch (EventHubException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
            System.out.println("Zwolniono egzekutor");
        }
    }
}
