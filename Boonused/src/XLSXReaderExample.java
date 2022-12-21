import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXReaderExample {
    public static void main(String[] args) {
        // ASJU MUUTA SIIN ALL //
        int year = 2022;
        int month = 1;

        int startdate = 1;
        int enddate = 31;
        // ASJU MUUTA SIIN YLEVAL //

        SortedMap<Integer, Integer> tuesday = new TreeMap<>();
        SortedMap<Integer, Integer> wednesday = new TreeMap<>();
        SortedMap<Integer, Integer> thursday = new TreeMap<>();
        SortedMap<Integer, Integer> friday = new TreeMap<>();
        SortedMap<Integer, Integer> saturday = new TreeMap<>();

        tuesday.put(1000, 0);
        tuesday.put(1200, 20);
        tuesday.put(1500, 35);
        tuesday.put(2000, 50);
        tuesday.put(2200, 80);
        tuesday.put(2500, 100);
        tuesday.put(2800, 125);
        tuesday.put(3000, 140);
        tuesday.put(3500, 150);
        tuesday.put(4000, 175);
        tuesday.put(4500, 215);
        tuesday.put(5000, 250);
        tuesday.put(5500, 290);
        tuesday.put(100000, 330);

        wednesday.put(500, 0);
        wednesday.put(750, 20);
        wednesday.put(1000, 35);
        wednesday.put(1250, 50);
        wednesday.put(1500, 70);
        wednesday.put(1750, 90);
        wednesday.put(2000, 105);
        wednesday.put(2500, 125);
        wednesday.put(3000, 160);
        wednesday.put(3500, 200);
        wednesday.put(4000, 240);
        wednesday.put(100000, 280);

        thursday.put(800, 0);
        thursday.put(1000, 16);
        thursday.put(1250, 25);
        thursday.put(1500, 35);
        thursday.put(2000, 50);
        thursday.put(2500, 80);
        thursday.put(3000, 120);
        thursday.put(3500, 155);
        thursday.put(4000, 190);
        thursday.put(4500, 225);
        thursday.put(5000, 260);
        thursday.put(50000, 300);

        friday.put(3000, 0);
        friday.put(3500, 30);
        friday.put(4000, 70);
        friday.put(4500, 120);
        friday.put(5000, 185);
        friday.put(6000, 225);
        friday.put(6500, 300);
        friday.put(7000, 340);
        friday.put(7500, 380);
        friday.put(8000, 420);
        friday.put(8500, 465);
        friday.put(9000, 510);
        friday.put(10000, 630);
        friday.put(100000, 800);

        saturday.put(2500, 0);
        saturday.put(3500, 25);
        saturday.put(4000, 70);
        saturday.put(4500, 120);
        saturday.put(5000, 180);
        saturday.put(6000, 225);
        saturday.put(6500, 300);
        saturday.put(7000, 340);
        saturday.put(7500, 380);
        saturday.put(8000, 420);
        saturday.put(8500, 465);
        saturday.put(9000, 510);
        saturday.put(10000, 630);
        saturday.put(100000, 800);

        ArrayList<Worker> workers = new ArrayList<>();
        ArrayList<Day> days = new ArrayList<>();


        try {
            File file = new File("C:\\Users\\mihke\\Documents\\GitHub\\naiiviBoonused\\aprill.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object

            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            Row row = itr.next();
            row = itr.next();


            Iterator<Cell> bonusReader = row.cellIterator();   //iterating over each column
            Cell cell = bonusReader.next();
            cell = bonusReader.next();

            // skipping columns until startdate
            int k = 1;
            while (k < startdate) {
                bonusReader.next();
                k++;
            }
            // reading columns until enddate
            while (bonusReader.hasNext() && k <= enddate) {
                // creating day objects and calculating their bonuses based on the day of the week and cash
                String weekday = String.valueOf(LocalDate.of(year, month, k).getDayOfWeek());

                cell = bonusReader.next();
//                System.out.println(cell);
                double kassa = cell.getNumericCellValue();
                double bonus = Objects.equals(weekday, "TUESDAY") ? calculateBonus(kassa, tuesday) :
                        Objects.equals(weekday, "WEDNESDAY") ? calculateBonus(kassa, wednesday) :
                                Objects.equals(weekday, "THURSDAY") ? calculateBonus(kassa, thursday) :
                                        Objects.equals(weekday, "FRIDAY") ? calculateBonus(kassa, friday) :
                                                Objects.equals(weekday, "SATURDAY") ? calculateBonus(kassa, saturday) : 0;
                days.add(new Day(k, kassa, bonus));
                k++;
            }

            // going through the rest of the table and gathering worker data
            int i = 0;
            while (itr.hasNext()) {
                row = itr.next();
                Iterator<Cell> nameReader = row.cellIterator();   //iterating over each column
                Cell name = nameReader.next();

                // reading worker name and registering them
                workers.add(new Worker(name.getStringCellValue()));

                row = itr.next();
                row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                int j = 1;
                cell = cellIterator.next();
                cell = cellIterator.next();


                while (j < startdate) {
                    cell = cellIterator.next();
                    j++;
                }

                // if the person worked that day, adding the day into their data and the worker and their hours into the days' data
                while (cellIterator.hasNext() && j <= enddate) {
                    cell = cellIterator.next();
                    double hours = cell.getNumericCellValue();
                    if (hours != 0.0) {
                        workers.get(i).addDay(j, hours);
                        days.get(j - startdate).addWorker(workers.get(i), hours);
                        days.get(j - startdate).addTotalHours(hours);
                    }
                    j++;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // going through days and their workers, adding bonus to worker objects.
        DecimalFormat f = new DecimalFormat("##.00");
        try {
            FileWriter myWriter = new FileWriter("jaanuar.txt");
            for (Day day : days) {
                double bonusFraction = day.getBonus() / day.getTotalHours();
                myWriter.write("PÄEV: " + day.getDay() +  "\n");
                myWriter.write("KASSA " + day.getCash()+  "\n");
                myWriter.write("BONUS " + day.getBonus()+  "\n");
                myWriter.write("IGA TUNNI KOHTA " + f.format(bonusFraction) + "€\n");
                myWriter.write("P2EVA TUNNID KOKKU " + day.getTotalHours()+  "\n");
                myWriter.write("-------------------------------------------------\n");
                HashMap<Worker, Double> map = day.getWorkerHours();
                for (Worker w : map.keySet()) {
                    myWriter.write(w.getNimi()+  "\n");
                    myWriter.write("TEHTUD TUNNID " + map.get(w)+  "\n");
                    myWriter.write("OSA BOONUSEST " + f.format(bonusFraction * map.get(w))+  "€\n");
                    myWriter.write("\n");
                    w.addBonus(bonusFraction * map.get(w));
                }
                myWriter.write("\n\n");
            }
            myWriter.write(String.valueOf(LocalDate.of(year,month,1).getMonth()) + "\n\n\n");
            for (Worker worker : workers) {
                myWriter.write(worker.getNimi()+ ": ");
                myWriter.write(f.format(worker.getBonus()) +  "€\n\n");
            }
            myWriter.close();

        }   catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static double calculateBonus(double cash, SortedMap<Integer, Integer> day) {
        double bonus = 0;
        for (Map.Entry mapElement : day.entrySet()) {
            int key = (int) mapElement.getKey();

            // Finding the value
            int value = (int) mapElement.getValue();
            if (cash < key) {
                bonus = value;
                break;
            }
        }
        return bonus;
    }
}  