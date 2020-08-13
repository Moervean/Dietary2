package Model;

public class Diet {
    String opis;
    String uwagi;
    String waga;

    public Diet(String opis, String uwagi, String waga) {
        this.opis = opis;
        this.uwagi = uwagi;
        this.waga = waga;
    }

    public Diet() {
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

    public String getWaga() {
        return waga;
    }

    public void setWaga(String waga) {
        this.waga = waga;
    }
}
