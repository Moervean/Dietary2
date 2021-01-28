package Model;

public class Workout {
    private String desc;
    private String warn;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public Workout() {
    }


    public Workout(String desc, String warn) {
        this.desc = desc;
        this.warn = warn;
    }
}
