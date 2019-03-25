package pl.piotrskiba.angularowo.utils;

import pl.piotrskiba.angularowo.R;

public class RankUtils {

    public static int getRankColorId(String rank){
        switch(rank){
            case "wlasciciel":
                return R.color.color_wlasciciel;
            case "admin":
                return R.color.color_admin;
            case "viceadmin":
                return R.color.color_viceadmin;
            case "jradmin":
                return R.color.color_jradmin;
            case "moderator":
                return R.color.color_moderator;
            case "pomocnik":
                return R.color.color_pomocnik;
            case "budowniczy":
                return R.color.color_budowniczy;
            case "sponsor":
                return R.color.color_sponsor;
            case "hrabia":
                return R.color.color_hrabia;
            case "legenda":
                return R.color.color_legenda;
            case "duchowny":
                return R.color.color_duchowny;
            case "kozak":
                return R.color.color_kozak;
            case "kasyniarz":
                return R.color.color_kasyniarz;
            case "eventowiec":
                return R.color.color_eventowiec;
            case "supervip":
                return R.color.color_supervip;
            case "kontovip":
                return R.color.color_kontovip;
            case "dziewczyna":
                return R.color.color_dziewczyna;
            case "chlopak":
                return R.color.color_chlopak;
            case "nolife":
                return R.color.color_nolife;
            case "stalygracz":
                return R.color.color_stalygracz;
            default:
                return R.color.color_gracz;
        }
    }

    public static boolean isStaffRank(String rank){
        switch(rank){
            case "wlasciciel":
                return true;
            case "admin":
                return true;
            case "viceadmin":
                return true;
            case "jradmin":
                return true;
            case "moderator":
                return true;
            case "pomocnik":
                return true;
            default:
                return false;
        }
    }
}
