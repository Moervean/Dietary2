package Model;

import java.util.List;

public class Uzytkownik {
    private String nickname;
    private String image;



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Uzytkownik(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public Uzytkownik() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Uzytkownik(String nickname) {
        this.nickname = nickname;
    }
}
