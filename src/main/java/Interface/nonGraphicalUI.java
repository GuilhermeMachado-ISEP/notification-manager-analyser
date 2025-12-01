package Interface;

import Domain.Option;
import Domain.Utils;

public class nonGraphicalUI implements Runnable{
    public void run(){
        start();
    }

    public void start(){
        System.out.println("############Non Graphical interface(Ideal for tests)#############");
        System.out.println("Please choose an option:");
        Runnable display = new notificationDisplayUI();
        Option[] options = {new Option(1, "Display Notifications", display), new Option(2,"Exit",null)};
        Utils.chooseOne(options).run();
    }

}
