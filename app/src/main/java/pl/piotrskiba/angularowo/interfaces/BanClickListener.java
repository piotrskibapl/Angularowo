package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.Ban;

public interface BanClickListener {

    void onBanClick(View view, Ban clickedBan);
}
