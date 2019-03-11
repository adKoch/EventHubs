package pl.sample.devices;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.time.Instant;
import java.util.List;

import com.microsoft.azure.functions.ExecutionContext;
import java.nio.charset.StandardCharsets;


public class Function {

    @FunctionName("EventReaction")
    public void eventHubProcessor(
            @EventHubTrigger(name = "eventHub",
                    eventHubName = "nazwa-mojego-centrum-zdarzen",
                    connection = "EHConsumerConnection")
                    List<String> events,
            final ExecutionContext context) {

        for (String data : events) {
            context.getLogger().info("Zdarzenie: " + data);
        }
    }

}
