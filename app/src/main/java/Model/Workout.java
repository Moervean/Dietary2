package Model;

public class Workout {
    private String opis;
    private String uwagi;

    public Workout() {
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    public Workout(String opis, String uwagi) {
        this.opis = opis;
        this.uwagi = uwagi;
    }
}
