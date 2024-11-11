package store;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        Manager manager = new Manager();
        PosSystem posMachine = new PosSystem(manager.itemsList);
        try {
            manager.setupStore();

            do{
                manager.showItems();
                posMachine.processCheckout(manager.itemsList, manager.promotionsList);
                manager.updateInventory(posMachine.getCartCurrentInventory());
//                posMachine.printReceipt();
            }while(posMachine.askContinue());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
