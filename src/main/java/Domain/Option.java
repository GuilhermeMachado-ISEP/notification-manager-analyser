package Domain;

public class Option {
    public int id;
    public String desc;
    public Runnable option;
    public Option(int id, String desc, Runnable option){
        this.id = id;
        this.desc = desc;
        this.option=option;
    }

    public void run(){
        option.run();
    }

    public String toString(){
        return String.format("%d: %s\n", id, desc);
    }
}
