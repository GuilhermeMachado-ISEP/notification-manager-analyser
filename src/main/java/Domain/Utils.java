package Domain;

import java.util.Scanner;

public class Utils {
    public static Option chooseOne(Option[] o){
        for(Option op: o){
            System.out.println(op.toString());
        }
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        while(choice<0 || choice>=o.length){
            System.out.println("Invalid choice");
            choice = sc.nextInt();
        }
        return o[choice-1];
    }
}
