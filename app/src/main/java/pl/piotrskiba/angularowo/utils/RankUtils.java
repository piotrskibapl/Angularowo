package pl.piotrskiba.angularowo.utils;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import pl.piotrskiba.angularowo.Constants;
import pl.piotrskiba.angularowo.models.Rank;

public class RankUtils {

    public static List<Rank> getAllRanks(){
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        String json = firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_RANKS_KEY);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, new TypeToken<List<Rank>>(){}.getType());
    }

    public static Rank getRankFromPreferences(Context context){
        String rankname = PreferenceUtils.getRankName(context);
        return getRankFromName(rankname);
    }

    public static Rank getRankFromName(String name){
        if(name == null || name.isEmpty())
            return null;

        List<Rank> ranks = getAllRanks();

        for(Rank rank : ranks){
            if(rank.getName().equals(name))
                return rank;
        }

        return new Rank(name, false, "7", "7");
    }
}
