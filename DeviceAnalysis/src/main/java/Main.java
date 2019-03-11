import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String args[]) throws InterruptedException, ExecutionException {
        EventProcessorManager manager= new EventProcessorManager(
                "nazwa-grupy-konsumentow",
                "nazwa-mojej-przestrzeni-nazw",
                "nazwa-mojego-centrum-zdarzen",
                "Consumer",
                "Consumer key",
                "Container connection",
                "nazwamojegomagazynu",
                "moj-podpis"
        );
        manager.register();
    }
}
