import java.util.HashMap;

public class Day {
    private int day;
    private HashMap<Worker, Double> workerHours = new HashMap<>();
    private double totalHours;
    private double cash;
    private double bonus;

    public Day(int day, double cash, double bonus) {
        this.day = day;
        this.cash = cash;
        this.bonus = bonus;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBonus() {
        return bonus;
    }

    public double getTotalHours() {
        return totalHours;
    }



    public void addWorker(Worker worker, double hours) {
        workerHours.put(worker, hours);
    }

    public HashMap<Worker, Double> getWorkerHours() {
        return workerHours;
    }

    public void addTotalHours(double additional) {
        this.totalHours += additional;
    }
}
