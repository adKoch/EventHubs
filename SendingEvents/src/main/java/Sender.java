import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
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


    public void sendEvents()
            throws EventHubException{

        final Gson gson = new GsonBuilder().create();

        System.out.println("Wysyłanie pojedynczo zdarzeń...");

            for (int i = 0; i < 200; i++) {
                Payload payload = new Payload("Single",Instant.now(), i);
                byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
                EventData sendEvent = EventData.create(payloadBytes);
                ehClient.sendSync(sendEvent);
            }
        System.out.println("Zakończono pojedyncze wysyłanie");
    }

    public void sendEventsBatch()
            throws EventHubException{

        final Gson gson = new GsonBuilder().create();

        System.out.println("Wysyłanie zdarzeń w partiach...");

        int counter=0;
            final EventDataBatch events = ehClient.createBatch();
            EventData sendEvent;
            do {
                final Payload payload = new Payload("Batch",Instant.now(),counter);
                final byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
                sendEvent = EventData.create(payloadBytes);
                counter++;
                } while(events.tryAdd(sendEvent));
            ehClient.sendSync(events);
            System.out.println("Partia: " + " rozmiar:  " + events.getSize());
        System.out.println("Zakończono wysyłanie w partiach");
    }

    public void sendEventsPartition(String partitionKey)
            throws EventHubException {

        final Gson gson = new GsonBuilder().create();

        System.out.println("Wysyłanie zdarzeń przez tę samą partycję");

        for (int i = 0; i < 20; i++) {
            Payload payload = new Payload("Same partition",Instant.now(), i);
            byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            ehClient.sendSync(sendEvent, partitionKey);
        }
        System.out.println("Zakończono wysyłanie przez tę samą partycję");
    }

    public void sendEventsSpecificPartition(String partitionNum)
            throws EventHubException {

        final Gson gson = new GsonBuilder().create();

        System.out.println("Wysyłanie zdarzeń przez specyficzną partycję");

        for (int i = 0; i < 20; i++) {
            Payload payload = new Payload("Partition: " + partitionNum,Instant.now(), i);
            byte[] payloadBytes = gson.toJson(payload).getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            PartitionSender sendingClient = ehClient.createPartitionSenderSync(partitionNum);
            sendingClient.sendSync(sendEvent);
        }
        System.out.println("Zakończono wysyłanie przez specyficzną partycję");
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
