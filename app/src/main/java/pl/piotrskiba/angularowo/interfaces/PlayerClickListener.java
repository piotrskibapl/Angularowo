package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.Player;

public interface PlayerClickListener {

    void onPlayerClick(View view, Player clickedPlayer);
}
