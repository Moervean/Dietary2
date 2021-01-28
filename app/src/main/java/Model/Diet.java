package Model;

public class Diet {
    String desc;
    String warn;
    String weight;

    public Diet(String desc, String warn, String weight) {
        this.desc = desc;
        this.warn = warn;
        this.weight = weight;
    }

    public Diet() {
    }

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
