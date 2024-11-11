package store;

import java.util.HashMap;
import java.util.List;

public class OutputView {
    HashMap<String, Item> items = new HashMap<>();

    OutputView(HashMap<String, Item> itemsList){
        this.items = itemsList;
    }

    public static void printWithFrame(List<String[]> list){
        for (String[] arr : list){
            String money = arr[2];
            if (!(arr[2].equals(" "))){
                int num = Integer.parseInt(arr[2]);
                money= OutputView.numberToString(num);
            }
            System.out.printf("%-19s%-8s%8s\n",arr[0],arr[1],money);
        }
    }

    public void printItemList(HashMap<String, Item> itemList) {
        for (String key : itemList.keySet()) {
            Item item = itemList.get(key);
            for (String promotion : item.inventoryList.keySet()) {
                int inventory = item.inventoryList.get(promotion);
                System.out.printf("- %s %s원 %s %s\n", item.name, numberToString(item.price), getInventory(inventory), getPromotion(promotion));
            }
            //System.out.println(item.promotion);
        }
        //출력까지 하기
    }

    public String getPromotion(String input){
        String nullPromotion = " ";
        if (input.equals("null")) {return nullPromotion;}
        return input;
    }

    public String getInventory(int num){
        String nullInventory = "재고 없음";
        if (num == 0) return nullInventory;
        return num+"개";
    }

    //1000 -> 1,000으로 바꾸기
    public static String numberToString(Integer num) {
        String str = num.toString();
        if (str.length() < 4) {return str;}
        int firstPosition = str.length() % 3;
        int dotCount = str.length() / 3;
        String sub = str.substring(0, firstPosition);

        for (int i = 0; i < dotCount; i++) {
            sub += "," + str.substring(firstPosition + 3 * i, firstPosition + 3 * (i + 1));
        }
        return sub;
    }


}