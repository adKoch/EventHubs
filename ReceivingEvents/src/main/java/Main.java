import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String args[]) throws InterruptedException, ExecutionException {
        String consumerGroupName = "consumers2";
        String namespaceName = "nazwa-mojej-przestrzeni-nazw";
        String eventHubName = "nazwa-mojego-centrum-zdarzen";
        String sasKeyName = "Consumer";        
        String sasKey = "Consumer key";
        String storageConnectionString =
               "container connection";
        String containerName = "nazwamojegomagazynu";
        String hostNamePrefix = "moj-podpis";

        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder()
                .setNamespaceName(namespaceName)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey);

        EventProcessorHost host = new EventProcessorHost(
                EventProcessorHost.createHostName(hostNamePrefix),
                eventHubName,
                consumerGroupName,
                eventHubConnectionString.toString(),
                storageConnectionString,
                containerName);

        System.out.println("Nazwa rejestrowanego hosta: " + host.getHostName());
        EventProcessorOptions eventProcessorOptions = new EventProcessorOptions();

        eventProcessorOptions.setExceptionNotification(exceptionArgs -> {
            System.out.println("Host: " + exceptionArgs.getHostname()
                    + " przy akcji: " + exceptionArgs.getAction()
                    + " błąd:  " + exceptionArgs.getException().toString());
        });

        host.registerEventProcessor(EventProcessor.class, eventProcessorOptions)
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
        System.out.println("Koniec pracy");
    }
}
