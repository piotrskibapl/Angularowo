package pl.piotrskiba.angularowo.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import pl.piotrskiba.angularowo.models.PlayerList

class PlayerListDiffCallback(
        private val oldList: PlayerList,
        private val newList: PlayerList
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.players[oldItemPosition].uuid == newList.players[newItemPosition].uuid
    }

    override fun getOldListSize(): Int {
        return oldList.players.size
    }

    override fun getNewListSize(): Int {
        return newList.players.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val p1 = oldList.players[oldItemPosition]
        val p2 = newList.players[newItemPosition]

        return p1.username == p2.username &&
                p1.rankKey == p2.rankKey &&
                p1.isVanished == p2.isVanished
    }

}