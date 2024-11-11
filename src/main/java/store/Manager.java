package store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    HashMap<String, Promotion> promotionsList = new HashMap<>();
    HashMap<String, Item> itemsList = new HashMap<>();

    public void setupStore(){
        try{
            setPromotions();
            setItems();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    //file 참조는 제대로 됨
    private void setPromotions() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        File file = new File(path+"/src/main/resources/promotions.md");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        while((line = bufferedReader.readLine()) != null){
            makePromotionsList(line);
        }
        bufferedReader.close();
    }

    private void makePromotionsList(String line) {
        try{
            Promotion p = createPromotion(line);
            String[] info = line.split(",");
            promotionsList.put(info[0], p);
        } catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private Promotion createPromotion(String line) throws IllegalArgumentException{
        String[] info = line.split(",");
        if (info.length != 5) throw new IllegalArgumentException("프로모션 파일 내의 프로모션 형식이 올바르지 않습니다.");

        Promotion promotion = new Promotion(info[0],info[1],info[2],info[3],info[4]);

        return promotion;
    }

    private void setItems() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        File file = new File(path+"/src/main/resources/products.md");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();
        while((line = bufferedReader.readLine()) != null){
            makeItemsList(line);
        }
        bufferedReader.close();
    }

    private void makeItemsList(String line) throws IllegalArgumentException {
        String[] info = line.split(",");
        List<String> trimInfo = new ArrayList<>();
        for(String string : info){
            trimInfo.add(string.trim());
        }

        String[] trimInfoArr = trimInfo.toArray(new String[info.length]);
        Item item = new Item();
        if (trimInfo.size() != 4){ throw new IllegalArgumentException("재고 파일 내의 재고 형식이 올바르지 않습니다.");}

        if (itemsList.containsKey(trimInfoArr[0])){
            item.addItemInfo(itemsList.get(trimInfoArr[0]),trimInfoArr);
        }
        if(!(itemsList.containsKey(trimInfoArr[0]))){
            item.createItem(trimInfoArr);
        }
        itemsList.put(trimInfoArr[0], item);
    }

    public void showItems(){
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
        OutputView outputView = new OutputView(this.itemsList);
        outputView.printItemList(outputView.items);
        System.out.println();
    }

    public void updateInventory(HashMap<String, Item> newInventory){
        this.itemsList = newInventory;
    }

}
