import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

import java.nio.charset.StandardCharsets;

public class EventProcessor implements IEventProcessor {
    private int checkpointCount = 0;

    @Override
    public void onOpen(PartitionContext context){
        System.out.println("Partycja: " + context.getPartitionId() + " -otwieranie");
    }

    @Override
    public void onClose(PartitionContext context, CloseReason reason){
        System.out.println("Partycja: " + context.getPartitionId() + " -zamykanie z powodu: " + reason.toString());
    }

    @Override
    public void onError(PartitionContext context, Throwable error){
        System.out.println("Partycja: " + context.getPartitionId() + " -błąd: " + error.toString());
    }

    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> events){
        System.out.println("Partycja: " + context.getPartitionId() + " odczyt partii");
        int eventCount = 0;
        for (EventData data : events) {
            try {
                System.out.println("Partycja: " + context.getPartitionId()
                        + " offset: " + data.getSystemProperties().getOffset()
                        + " numer sekwencji: " + data.getSystemProperties().getSequenceNumber()
                        + "\n\tzdarzenie: " + new String(data.getBytes(), StandardCharsets.UTF_8));
                eventCount++;

                this.checkpointCount++;
                if ((checkpointCount % 5) == 0) {
                    System.out.println("Partycja: " + context.getPartitionId()
                            + " checkpoint: " + data.getSystemProperties().getOffset()
                            + " numer sekwencji: " + data.getSystemProperties().getSequenceNumber());
                    context.checkpoint(data).get();
                }
            } catch (Exception e) {
                System.out.println("Wyjątek przetwarzania zdarzenia: " + e.toString());
            }
        }
        System.out.println("Partycja: " + context.getPartitionId()
                + " liczba zdarzeń: " + eventCount
                + " host: " + context.getOwner());
    }
}