package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardList {
    @SerializedName("campaigns")
    private List<Reward> rewards;

    public RewardList(List<Reward> rewards){
        this.rewards = rewards;
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
