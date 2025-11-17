package Interface;

import Domain.Option;

public class nonGraphicalUI extends Runnable{
    public void run(){

    }

    public void start(){
        System.out.println("############Non Graphical interface(Ideal for teses)#############");
        Option[] options = {new Option(1,"Exit",null)};
    }

}
