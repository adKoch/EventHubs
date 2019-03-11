public class Main {
    public static void main(String args[]) {
        Sender sender;
        try {
            sender = new Sender("nazwa-mojej-przestrzeni-nazw",
                    "nazwa-mojego-centrum-zdarzen",
                    "Publisher",
                    "Publisher key");
            try{
                System.out.println("Wysyłanie zdarzeń");
                sender.sendRandomDeviceData();
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
