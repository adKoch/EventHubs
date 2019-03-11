
public class Main {

    public static void main(String[] args){
        Sender sender;
        try {
            sender = new Sender("nazwa-mojej-przestrzeni-nazw",
                    "nazwa-mojego-centrum-zdarzen",
                    "Publisher",
                    "Publisher Key");

            try{
                sender.sendEvents();
                sender.sendEventsSpecificPartition("0");
                //sender.sendEventsBatch();
                sender.sendEventsPartition("abcd");
                //sender.sendEventsSpecificPartition("1");
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                sender.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
