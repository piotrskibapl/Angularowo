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
                case "moderator":
                    return R.color.color_minecraft_2;
                case "pomocnik":
                    return R.color.color_minecraft_9;
                case "budowniczy":
                    return R.color.color_minecraft_b;
                case "eventowiec":
                    return R.color.color_minecraft_a;
                case "duchowny":
                    return R.color.color_minecraft_d;
                case "ultravip":
                    return R.color.color_minecraft_e;
                case "supervip":
                    return R.color.color_minecraft_a;
                case "kontovip":
                    return R.color.color_minecraft_6;
                case "chlopak":
                    return R.color.color_minecraft_5;
                case "dziewczyna":
                    return R.color.color_minecraft_d;
                case "nolife":
                    return R.color.color_minecraft_3;
                case "stalygracz":
                    return R.color.color_minecraft_3;
                case "gracz":
                    return R.color.color_minecraft_7;
                case "nowygracz":
                    return R.color.color_minecraft_7;
                default:
                    return R.color.color_minecraft_7;
            }
        }
        else{
            return R.color.color_minecraft_7;
        }
    }

    public static int getRankChatColorId(String rank){
        if(rank != null) {
            switch (rank) {
                case "wlasciciel":
                    return R.color.color_minecraft_b;
                case "admin":
                    return R.color.color_minecraft_c;
                case "moderator":
                    return R.color.color_minecraft_a;
                case "pomocnik":
                    return R.color.color_minecraft_b;
                case "budowniczy":
                    return R.color.color_minecraft_f;
                case "eventowiec":
                    return R.color.color_minecraft_b;
                case "duchowny":
                    return R.color.color_minecraft_e;
                case "ultravip":
                    return R.color.color_minecraft_f;
                case "supervip":
                    return R.color.color_minecraft_f;
                case "kontovip":
                    return R.color.color_minecraft_f;
                case "chlopak":
                    return R.color.color_minecraft_f;
                case "dziewczyna":
                    return R.color.color_minecraft_f;
                case "nolife":
                    return R.color.color_minecraft_f;
                case "stalygracz":
                    return R.color.color_minecraft_f;
                case "gracz":
                    return R.color.color_minecraft_f;
                case "nowygracz":
                    return R.color.color_minecraft_f;
                default:
                    return R.color.color_minecraft_f;
            }
        }
        else{
            return R.color.color_minecraft_f;
        }
    }

    public static boolean isStaffRank(String rank){
        if(rank != null) {
            switch (rank) {
                case "wlasciciel":
                    return true;
                case "admin":
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
