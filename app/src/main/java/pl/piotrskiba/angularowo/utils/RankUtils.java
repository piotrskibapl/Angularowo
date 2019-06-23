package pl.piotrskiba.angularowo.utils;

import pl.piotrskiba.angularowo.R;

public class RankUtils {

    public static int getRankColorId(String rank){
        if(rank != null) {
            switch (rank) {
                case "wlasciciel":
                    return R.color.color_minecraft_4;
                case "admin":
                    return R.color.color_minecraft_4;
                case "viceadmin":
                    return R.color.color_minecraft_c;
                case "moderator":
                    return R.color.color_minecraft_2;
                case "pomocnik":
                    return R.color.color_minecraft_9;
                case "budowniczy":
                    return R.color.color_minecraft_b;
                case "sponsor":
                    return R.color.color_minecraft_e;
                case "hrabia":
                    return R.color.color_minecraft_b;
                case "legenda":
                    return R.color.color_minecraft_e;
                case "duchowny":
                    return R.color.color_minecraft_d;
                case "pro":
                    return R.color.color_minecraft_9;
                case "noob":
                    return R.color.color_minecraft_9;
                case "kasyniarz":
                    return R.color.color_minecraft_c;
                case "eventowiec":
                    return R.color.color_minecraft_a;
                case "supervip":
                    return R.color.color_minecraft_a;
                case "kontovip":
                    return R.color.color_minecraft_6;
                case "dziewczyna":
                    return R.color.color_minecraft_d;
                case "chlopak":
                    return R.color.color_minecraft_5;
                case "nolife":
                    return R.color.color_minecraft_3;
                case "stalygracz":
                    return R.color.color_minecraft_3;
                default:
                    return R.color.color_minecraft_7;
            }
        }
        else{
            return R.color.color_minecraft_7;
        }
    }

    public static boolean isStaffRank(String rank){
        if(rank != null) {
            switch (rank) {
                case "wlasciciel":
                    return true;
                case "admin":
                    return true;
                case "viceadmin":
                    return true;
                case "moderator":
                    return true;
                case "pomocnik":
                    return true;
                default:
                    return false;
            }
        }
        else{
            return false;
        }
    }
}
