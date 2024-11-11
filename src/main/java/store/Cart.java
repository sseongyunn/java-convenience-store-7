package store;

import static camp.nextstep.edu.missionutils.DateTimes.now;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Cart {
    HashMap<String, Item> currentInventory;
    HashMap<String, Promotion> promotionList;

    HashMap<String,Integer> generalCheckout = new HashMap<>();
    HashMap<String,Integer> promotionCheckout = new HashMap<>();
    HashMap<String,Integer> gift = new HashMap<>();

    Cart(HashMap<String, Item> itemsList,HashMap<String, Promotion> promotionsList){
        this.currentInventory = itemsList;
        this.promotionList = promotionsList;
    }

    Cart(){}

    public void addItem(String input)throws Exception{
        try{
            Boolean promoApply = checkValidation(input);
            String[] checkoutCount =checkDiscounts(promoApply, input);
            updateCurrentInventory(checkoutCount);
            //재고 업데이트
        } catch(Exception e){
            throw e;
        }
    }

    public void updateCurrentInventory(String[] checkoutList){
        String itemName = checkoutList[0];
        int generalCheckout = Integer.parseInt(checkoutList[1]);
        int promotionCheckout = Integer.parseInt(checkoutList[2]);

        Item currentItem = currentInventory.get(itemName);
        int originalPromotion = currentItem.inventoryList.get(currentItem.promotion);
        int originalGeneral = currentItem.inventoryList.get("null");

        if(generalCheckout+promotionCheckout <= originalPromotion){
            currentItem.inventoryList.put(currentItem.promotion, originalPromotion-promotionCheckout-generalCheckout);
        }
        if(generalCheckout+promotionCheckout > originalPromotion){
            currentItem.inventoryList.put(currentItem.promotion, 0);
            currentItem.inventoryList.put("null", generalCheckout+promotionCheckout-originalPromotion);
        }
        currentInventory.put(itemName, currentItem);
    }

    private String[] checkDiscounts(Boolean promo, String input) throws Exception{
        try{
            String name = getInfo(input)[0];
            Integer quantity = Integer.parseInt(getInfo(input)[1]);

            if (promo == false){
                generalCheckout.put(name, quantity);
                return new String[]{name,String.valueOf(quantity),"0","0"};
            }// 프로모션 적용 안 되는 경우는 모두 일반 결제로 계산된다.
            if (promo == true) {
                Integer[] num = getPromotionCheckout(name,quantity);
                //System.out.printf("%d %d %d",num[0],num[1],num[2]);
                generalCheckout.put(name,num[0]);
                promotionCheckout.put(name, num[1]);
                gift.put(name, num[2]);
                return new String[]{name, String.valueOf(num[0]), String.valueOf(num[1]), String.valueOf(num[2])};
            }
        }catch(Exception e){
            throw e;
        }
        return new String[4];
    }

    //promotionCheckout, gift 해시맵에 수량을 넣는 기능
    private Integer[] getPromotionCheckout(String name, Integer quantity)throws IllegalArgumentException{
        Item item = currentInventory.get(name);
        //System.out.println(item.promotion);
        Integer promoThreshold = promotionList.get(item.promotion).buy;
        int promoUnit = promoThreshold + 1;
        int generalCheckout;
        int promotionCheckout;
        int gift;

        if(quantity < item.inventoryList.get(item.promotion)){
            int promoPair = quantity/promoUnit;
            promotionCheckout = promoPair*promoUnit;
            gift = promoPair;
            generalCheckout = quantity - promotionCheckout;
            //System.out.printf("%d %d %d",promotionCheckout,gift,generalCheckout);

            if(quantity-promotionCheckout == promoThreshold){
                int num = InputView.getAnswer1(name);
                gift += num;
                promotionCheckout += num*promoUnit;
                generalCheckout -= num*promoThreshold;
                //System.out.printf("%d %d %d",promotionCheckout,gift,generalCheckout);
            }
            return new Integer[]{generalCheckout, promotionCheckout, gift};
        }

        int promoPair = item.inventoryList.get(item.promotion)/promoUnit;
        promotionCheckout = promoPair*promoUnit;
        gift = promoPair;
        generalCheckout = (quantity - promotionCheckout)*InputView.getAnswer2(name, quantity-promotionCheckout);
        //System.out.printf("%d %d %d",promotionCheckout,gift,generalCheckout);

        return new Integer[]{generalCheckout, promotionCheckout, gift};
    }

    public Boolean checkValidation(String string) throws Exception{
        try{
            Boolean format = checkFormat(string);
            String itemName = getInfo(string)[0];
            int quantity = Integer.parseInt(getInfo(string)[1]);

            Boolean existance = checkExistence(itemName);
            Boolean validQauntity = checkQauntity(itemName, quantity);
            Boolean validPromotion = checkPromotionPeriod(itemName);
            return validPromotion;
        }catch(Exception e){
            throw e;
        }
    }

    private String[] getInfo(String input){
        int index = input.lastIndexOf("-");
        String itemName = input.substring(1,index).trim();
        String quantity = input.substring(index+1,input.length()-1).trim();

        return new String[]{itemName, quantity};
    }

    private boolean checkFormat(String string)throws IllegalArgumentException{
        // ^\[\s*[가-힣|\s|a-zA-Z]+\s*-\s*[1-9]+[0-9]*\s*\]$
        if(string.matches("^\\[\\s*[가-힣|\\s|a-zA-Z]+\\s*-\\s*[1-9]+[0-9]*\\s*\\]$")){
            return true;
        }
        throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요. \n상품명은 한글과 영어, 공백으로 구성되어야 하며, 수량은 정수값이어야 합니다.");
    }

    private boolean checkExistence(String string)throws IllegalArgumentException{
        if (currentInventory.containsKey(string)){
            return true;
        }
        throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    private boolean checkQauntity(String itemName, int quantity){
        Item item = currentInventory.get(itemName);
        HashMap<String, Integer> inventory = item.inventoryList;
        int total = 0;

        for(String promotion : inventory.keySet()){ total += inventory.get(promotion); }

        if (quantity <= total){
            return true;
        }
        throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    private boolean checkPromotionPeriod(String itemName){
        String promotionName = currentInventory.get(itemName).promotion;
        if (promotionName.equals("null")) return false;

        Promotion promotion = promotionList.get(promotionName);

        LocalDateTime now = now();
        try{
            LocalDateTime start = LocalDateTime.parse(promotion.start_date+"T00:00:00.000000");
            LocalDateTime end = LocalDateTime.parse(promotion.end_date+"T23:59:59.999999");

            if(now.isAfter(start) && now.isBefore((end))) return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }



}
