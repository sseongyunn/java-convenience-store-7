package store;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class InputView {

    public static String getInput(){
        String input = readLine();
        return input;
    }

    public static int getAnswer1(String item)throws IllegalArgumentException{
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)",item);
        try{
            String input = readLine();
            if (input.equals("Y")|input.equals("y")) return 1;
            if (input.equals("N")|input.equals("n")) return 0;
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }catch(IllegalArgumentException e){
            throw e;
        }

    }

    public static int getAnswer2(String item,Integer remainder)throws IllegalArgumentException{
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",item,remainder);
        try{
            String input = readLine();
            if (input.equals("Y")|input.equals("y")) return 1;
            if (input.equals("N")|input.equals("n")) return 0;
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }catch(IllegalArgumentException e){
            throw e;
        }
    }

    public static boolean getAnswer3(){
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        try{
            String input = readLine();
            if (input.equals("Y")|input.equals("y")) return true;
            if (input.equals("N")|input.equals("n")) return false;
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }catch(IllegalArgumentException e){
            throw e;
        }
    }

    public static boolean getAnswer4(){
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        try{
            String input = readLine();
            if (input.equals("Y")|input.equals("y")) return true;
            if (input.equals("N")|input.equals("n")) return false;
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }catch(IllegalArgumentException e){
            throw e;
        }
    }
}
