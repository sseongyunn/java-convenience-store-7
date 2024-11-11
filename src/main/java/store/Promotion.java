package store;

public class Promotion {
    String name;
    Integer buy;
    int get;
    String start_date;
    String end_date;

    Promotion(String name, String buy, String get, String start_date, String end_date){
        this.name = name;
        this.buy = Integer.parseInt(buy);
        this.get = Integer.parseInt(get);
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
