package pl.piotrskiba.angularowo.utils;

import android.content.Context;

import java.util.Arrays;

import pl.piotrskiba.angularowo.R;

public class RankUtils {

    public static int getRankColor(Context context, String rank){
        if(rank != null) {
            String[] ranks_team = context.getResources().getStringArray(R.array.ranks_team_ids);
            String[] ranks_other = context.getResources().getStringArray(R.array.ranks_other_ids);

            if(Arrays.asList(ranks_team).contains(rank)){
                int[] colors = context.getResources().getIntArray(R.array.ranks_team_colors);
                return colors[Arrays.asList(ranks_team).indexOf(rank)];
            }
            else if(Arrays.asList(ranks_other).contains(rank)){
                int[] colors = context.getResources().getIntArray(R.array.ranks_other_colors);
                return colors[Arrays.asList(ranks_other).indexOf(rank)];
            }
            else{
                return R.color.color_minecraft_7;
            }
        }
        else{
            return R.color.color_minecraft_7;
        }
    }

    public static int getRankChatColor(Context context, String rank){
        if(rank != null) {
            String[] ranks_team = context.getResources().getStringArray(R.array.ranks_team_ids);
            String[] ranks_other = context.getResources().getStringArray(R.array.ranks_other_ids);

            if(Arrays.asList(ranks_team).contains(rank)){
                int[] colors = context.getResources().getIntArray(R.array.ranks_team_chat_colors);
                return colors[Arrays.asList(ranks_team).indexOf(rank)];
            }
            else if(Arrays.asList(ranks_other).contains(rank)){
                int[] colors = context.getResources().getIntArray(R.array.ranks_other_chat_colors);
                return colors[Arrays.asList(ranks_other).indexOf(rank)];
            }
            else{
                return R.color.color_minecraft_f;
            }
        }
        else{
            return R.color.color_minecraft_f;
        }
    }

    public static String getRankName(Context context, String rank){
        if(rank != null) {
            String[] ranks_team = context.getResources().getStringArray(R.array.ranks_team_ids);
            String[] ranks_other = context.getResources().getStringArray(R.array.ranks_other_ids);

            if(Arrays.asList(ranks_team).contains(rank)){
                String[] names = context.getResources().getStringArray(R.array.ranks_team_names);
                return names[Arrays.asList(ranks_team).indexOf(rank)];
            }
            else if(Arrays.asList(ranks_other).contains(rank)){
                String[] names = context.getResources().getStringArray(R.array.ranks_other_names);
                return names[Arrays.asList(ranks_other).indexOf(rank)];
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    public static boolean isStaffRank(Context context, String rank){
        if(rank != null) {
            String[] ranks_team = context.getResources().getStringArray(R.array.ranks_team_ids);
            if(Arrays.asList(ranks_team).contains(rank))
                return true;
            else
                return false;
        }
        else{
            return false;
        }
    }
}
