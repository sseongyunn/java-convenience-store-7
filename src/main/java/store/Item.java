package store;

import java.util.HashMap;

public class Item {

    String name;
    int price;
    String promotion;
    HashMap<String, Integer> inventoryList = new HashMap<>();

    Item(){};

    Item(String name, String price){
        this.name = name;
        this.price = Integer.parseInt(price);
    }

    void addItemInfo(Item original, String[] info){
        this.name = info[0];
        this.price = Integer.parseInt(info[1]);
        if (!(info[3].equals("null"))){
            this.promotion = info[3];
        }
        if(info[3].equals("null")){
            this.promotion = original.promotion;
        }

        for(String key : original.inventoryList.keySet()){
            this.inventoryList.put(key,original.inventoryList.get(key));
        }
        this.inventoryList.put(info[3],Integer.parseInt(info[2]));
    }

    void createItem(String[] info){
        this.name = info[0];
        this.price = Integer.parseInt(info[1]);
        this.promotion = info[3];
        this.inventoryList.put(info[3],Integer.parseInt(info[2]));
        if (!(promotion.equals("null"))){
            this.inventoryList.put("null", 0);
        }
    }

    //총 재고 구하기

//    public String getPromotion(String input){
//        if (input == "null"){
//             return new String("");
//        }
//        return input;
//    }

    public void setInventory(String inventory){
        int num = Integer.parseInt(inventory);
        inventoryList.put(promotion,num);
    }

    public void addInventory(String promotion, String inventory){
        int num = Integer.parseInt(inventory);
        inventoryList.put(promotion, num);
    }
}
