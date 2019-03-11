public class Analyzer {

    private int onCount=0;
    private int offCount=0;
    private int standbyCount=0;
    private int crashCount=0;
    private int totalCount=0;

    public void passStatus(String status){

        totalCount++;

        switch (status) {
            case "Aktywacja":
                onCount++;
                break;
            case "Deaktywacja":
                offCount++;
                break;
            case "Czuwanie":
                standbyCount++;
                break;
            case "Awaria":
                crashCount++;
                break;
        }
    }
    public String printAnalysis(){
        return "Włączonych "+ onCount
                + "\nWyłączonych: " + offCount
                + "\nCzuwających: " + standbyCount
                + "\nZepsutych: " + crashCount
                + "\n/" + totalCount;
    }
    public int getTotalCount(){
        return totalCount;
    }

    public int getOnCount() {
        return onCount;
    }

    public int getOffCount() {
        return offCount;
    }

    public int getStandbyCount() {
        return standbyCount;
    }

    public int getCrashCount() {
        return crashCount;
    }


}
