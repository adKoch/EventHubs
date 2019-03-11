package pl.ehfunc.sample;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.util.List;


public class EventHubFunction {

    @FunctionName("eventHubTrig")
    public void eventHubProcessor(
            @EventHubTrigger(name = "message",
                    eventHubName = "nazwa-mojego-centrum-zdarzen",
                    connection = "AppConnection")
                    String message, final ExecutionContext context){
            context.getLogger().info(message);
    }
}