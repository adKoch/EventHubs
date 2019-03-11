import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;

import java.util.concurrent.ExecutionException;

public class EventProcessorManager {
    private EventProcessorHost host;
    private EventProcessorOptions options;

    public EventProcessorManager(
            String consumerGroupName,
            String namespaceName,
            String eventHubName,
            String sasKeyName,
            String sasKey,
            String storageConnectionString,
            String containerName,
            String hostNamePrefix) {

        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder()
                .setNamespaceName(namespaceName)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey);

        host = new EventProcessorHost(
                EventProcessorHost.createHostName(hostNamePrefix),
                eventHubName,
                consumerGroupName,
                eventHubConnectionString.toString(),
                storageConnectionString,
                containerName);

        EventProcessorOptions eventProcessorOptions = new EventProcessorOptions();

        eventProcessorOptions.setExceptionNotification(exceptionArgs -> {
            System.out.println("Host: " + exceptionArgs.getHostname()
                    + " przy akcji: " + exceptionArgs.getAction()
                    + " błąd:  " + exceptionArgs.getException().toString());
        });
        options=eventProcessorOptions;
    }

    public void register() throws ExecutionException, InterruptedException {
        host.registerEventProcessor(EventProcessor.class, options)
                .whenComplete((unused, e) ->
                {
                    if (e != null) {
                        System.out.println("Błąd rejestracji:  " + e.toString());
                        if (e.getCause() != null) {
                            System.out.println("Powód:  " + e.getCause().toString());
                        }
                    }
                })
                .thenAccept((unused) ->
                {
                    System.out.println("Naciśnij enter, aby zatrzymać program");
                    try {
                        System.in.read();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .thenCompose((unused) ->
                {
                    //Dla pomyślnej rejestracji
                    return host.unregisterEventProcessor();
                })
                .exceptionally((e) ->
                {
                    System.out.println("Błąd cofania rejestracji: " + e.toString());
                    if (e.getCause() != null) {
                        System.out.println("Powód: " + e.getCause().toString());
                    }
                    return null;
                })
                .get();
    }


}
