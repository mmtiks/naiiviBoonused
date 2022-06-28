import java.util.HashMap;
import java.util.Map;

public class Worker {
    private final String nimi;
    private double bonus;
    private Map<Integer,Double> days = new HashMap<>();

    public Worker(String nimi) {
        this.nimi = nimi;
        this.bonus = 0;
    }

    public String getNimi() {
        return nimi;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public void addBonus(double amount) {
        this.bonus += amount;
    }

    public void addDay(int day, double length) {
        days.put(day,length);
    }

    public void getDays() {
        for (Integer integer : days.keySet()) {
            System.out.println(integer + " " + days.get(integer));
        }
    }
}
