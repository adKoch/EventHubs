import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

import java.nio.charset.StandardCharsets;

public class EventProcessor implements IEventProcessor {
    private int checkpointCount = 0;
    private Analyzer analyzer;
    private Gson gson;

    @Override
    public void onOpen(PartitionContext context){
        System.out.println("Partycja: " + context.getPartitionId() + " -otwieranie");
        if(null==analyzer) analyzer=new Analyzer();
        if(null==gson){
            gson = new GsonBuilder().create();
        }
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
        DeviceData deviceData;
        for (EventData data : events) {
            try {
                deviceData=gson.fromJson(new String(data.getBytes(), StandardCharsets.UTF_8),DeviceData.class);
                analyzer.passStatus(deviceData.getNewStatus());

                this.checkpointCount++;
                if ((checkpointCount % 5) == 0) {
                    context.checkpoint(data).get();
                }
                System.out.println(analyzer.printAnalysis());

            } catch (Exception e) {
                System.out.println("Wyjątek przetwarzania zdarzenia: " + e.toString());
            }
        }
    }

}