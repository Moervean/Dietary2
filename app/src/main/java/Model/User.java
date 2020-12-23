package Model;

import java.util.List;

public class User {
    private String nickname;
    private String image;



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }
}
