package Model;

public class Exercise {
    private String name;
    private String desc;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Exercise() {
    }

    public Exercise(String name, String desc, String url) {
        this.name = name;
        this.desc = desc;
        this.url = url;
    }
}
