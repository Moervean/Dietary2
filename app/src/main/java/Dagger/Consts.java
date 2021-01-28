package Dagger;

import javax.inject.Singleton;


@Singleton
public class Consts implements DaggerInterface {


    public Consts(){

    }


    public final String users = "users";
    public final String desc = "desc";
    public final String warn = "warn";
    public final String weight = "weight";
    public final String proteges = "proteges";
    public final String coaches = "coaches";
    public final String diet = "diet";
    public final String workout = "workout";
    public final String nickname = "nickname";
    public final String image = "image";
    public final String dinner = "dinner";
    public final String friend_request = "Friends_request";
    public final String ID = "ID";
    public final String priv = "priv";
    public final String _default = "default";
    public final String noOne = "noone";
    public final String all = "all";
    public final String friends = "friends";
    public final String profile_pictures = "Profile_Pictures";
    public final String request_send = "Request_Send";


    @Override
    public Consts getConsts() {
        return this;
    }
}
