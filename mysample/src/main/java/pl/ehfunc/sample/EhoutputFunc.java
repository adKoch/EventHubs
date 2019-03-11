package pl.ehfunc.sample;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class EhoutputFunc{
    @FunctionName("sendEvent")
    @EventHubOutput(name = "event", eventHubName = "nazwa-mojego-centrum-zdarzen", connection = "AppConnection")
    public String sendTime(
            @TimerTrigger(name = "sendTimeTrigger", schedule = "*/20 * * * * *") String timerInfo)  {
        return "TrescZdarzenia";
    }
}