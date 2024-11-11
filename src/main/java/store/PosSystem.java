package store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PosSystem {
    HashMap<String, Item> originalInventory;

    PosSystem(HashMap<String, Item> inventory){
        this.originalInventory = inventory;
    }

    public void processCheckout(HashMap<String, Item> itemsList, HashMap<String, Promotion> promotionsList){
        String[] itemsArray = getUserInput();
        try{
            Cart cart = new Cart(itemsList,promotionsList);
            for(String item : itemsArray){
                cart.addItem(item);
            }
            this.originalInventory = cart.currentInventory;
            printReceipt(cart);
        }catch(Exception e){
            System.out.println(e.getMessage());
            //초기화를 위해 originalInventory 세팅
            this.originalInventory = itemsList;
        }
        // 영수증 출력 함수
    }

    public void printReceipt(Cart cart){
        // [상품명, 수량, 금액] 배열의 리스트 구해오기(cart 객체)
        List<String[]> purchaseList = getPurchaseProductList(cart);
        // 증정품의 [상품명, 수량, 금액(" ")] 배열의 리스트 구해오기(cart 객체)
        List<String[]> giftList = getGiftList(cart);
        // 총구매액, 행사 할인, 멤버쉽할인, 내실 돈을 [이름, 수량(" " 또는 수량), 금액의 리스트 구해오기(cart 객체)
        List<String[]> moneyReport = getMoneyReport(purchaseList, cart);

        //편의점 구분선 출력하기
        System.out.println("==============W 편의점================");
        System.out.printf("%-19s%-7s%7s\n","상품명","수량","금액");
        // 틀에 맞춰 출력하기(output 객체)
        OutputView.printWithFrame(purchaseList);
        //증정 구분선 출력하기
        System.out.println("=============증\t\t정===============");
        // 틀에 맞춰 출력하기(output 객체)
        OutputView.printWithFrame(giftList);
        System.out.println("====================================");
        // 틀에 맞춰 출력하기(output 객체)
        OutputView.printWithFrame(moneyReport);

    }

    private List<String[]> getMoneyReport(List<String[]> purchaseList, Cart cart){
        List<String[]> moneyReport = new ArrayList<>();

        int totalMoney = getTotalMoney(purchaseList);
//        System.out.println("success totalmoney");
        int totalCount = getTotalCount(purchaseList);
//        System.out.println("success totalcount");

        moneyReport.add(new String[]{"총구매액", String.valueOf(totalCount), String.valueOf(totalMoney)});
//        System.out.println(moneyReport.get(0)); //ddd
        int promotionDiscount = 0-getPromotionDiscount(cart);
        moneyReport.add(new String[]{"행사할인"," ",String.valueOf(promotionDiscount)});
//        System.out.println(moneyReport.get(1)); //ddd
        int membershipDiscount = 0-getMembershipDiscount(cart);
        moneyReport.add(new String[]{"멤버십할인"," ", String.valueOf(membershipDiscount)});
//        System.out.println(moneyReport.get(2)); //ddd
        int totalPayment = totalMoney+promotionDiscount+membershipDiscount;
        moneyReport.add(new String[]{"내실돈"," ", String.valueOf(totalPayment)});
//        System.out.println(moneyReport.get(3)); //ddd
        return moneyReport;
    }


    private Integer getMembershipDiscount(Cart cart) {
        if (InputView.getAnswer4()) {
            int total = 0;
            for(Integer num :cart.generalCheckout.values()) {
                total += num;
            }
            return total*3/10;
        }
        return 0;
    }


    private int getPromotionDiscount(Cart cart){
        int money = 0;
        for (String name : cart.gift.keySet()) {
            money += cart.gift.get(name) * (cart.currentInventory.get(name).price);
        }
        return money;
    }

    private int getTotalMoney(List<String[]> purchaseList){
        int money = 0;
        for(String[] arr : purchaseList){
            money += Integer.parseInt(arr[2]);
        }
        return money;
    }

    private int getTotalCount(List<String[]> purchaseList){
        int count = 0;
        for(String[] arr : purchaseList){
            count += Integer.parseInt(arr[1]);
        }
        return count;
    }

    private List<String[]> getGiftList(Cart cart){
        List<String[]> giftList = new ArrayList<>();
        Set<String> giftnameList = cart.gift.keySet();
        for(String name : giftnameList){
            int count = cart.gift.get(name);
            String[] arr = new String[]{name, String.valueOf(count), " "};
            giftList.add(arr);
        }
        return giftList;
    }

    private List<String[]> getPurchaseProductList(Cart cart){
        List<String[]> purchaseList = new ArrayList<>();
        Set<String> purchaseProduct = getPurchaseProduct(cart);
        for(String name : purchaseProduct){
            int total = getPurchaseQuantity(name,cart);
            int money = cart.currentInventory.get(name).price;
            String[] arr = new String[]{name, String.valueOf(total), String.valueOf(money*total)};
            purchaseList.add(arr);
        }
        return purchaseList;
    }

    private Set<String> getPurchaseProduct(Cart cart){
        Set<String> generalProduct = cart.generalCheckout.keySet();
        Set<String> promotionProduct = cart.promotionCheckout.keySet();
        Set<String> purchaseProduct = new HashSet<>();
        purchaseProduct.addAll(generalProduct);
        purchaseProduct.addAll(promotionProduct);

        return purchaseProduct;
    }

    private int getPurchaseQuantity(String name, Cart cart){
        return cart.generalCheckout.get(name)+cart.promotionCheckout.get(name);
    }



    public HashMap<String, Item> getCartCurrentInventory(){
        return this.originalInventory;
    }

    private String[] getUserInput(){
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String line = InputView.getInput();
        String[] rawInput = line.split(",");
        String[] inputArr = new String[line.split(",").length];

        for (int i = 0; i<inputArr.length; i++){
            inputArr[i] = rawInput[i].trim();
        }
        return inputArr;
    }

    public boolean askContinue(){
        boolean answer = true;
        try{
            answer = InputView.getAnswer3();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return answer;
    }
}
