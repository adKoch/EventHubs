package pl.sample.devices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.Instant;

import com.microsoft.azure.functions.annotation.*;

public class DeviceDataGeneratorFunc {

    @FunctionName("sendEvent")
    @EventHubOutput(name = "event", eventHubName = "nazwa-mojego-centrum-zdarzen", connection = "EHPublisherConnection")
    public String sendTime(
            @TimerTrigger(name = "sendTimeTrigger", schedule = "*/20 * * * * *") String timerInfo)  {
        Gson gson = new GsonBuilder().create();
        DeviceData deviceData = new RandomDeviceDataGenerator().generateRandomDeviceData();
        return gson.toJson(deviceData);
    }
}

