package pl.piotrskiba.angularowo.adapters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import pl.piotrskiba.angularowo.models.BanList

class BanListDiffCallback(
        private val oldList: BanList,
        private val newList: BanList
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // TODO: replace the hashCode with ban ID when provided by backend
        return oldList.banList[oldItemPosition].hashCode() == newList.banList[newItemPosition].hashCode()
    }

    override fun getOldListSize(): Int {
        return oldList.banList.size
    }

    override fun getNewListSize(): Int {
        return newList.banList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val b1 = oldList.banList[oldItemPosition]
        val b2 = newList.banList[newItemPosition]

        return b1.banTime == b2.banTime &&
                b1.banner == b2.banner &&
                b1.expireDate == b2.expireDate &&
                b1.reason == b2.reason &&
                b1.type == b2.type &&
                b1.username == b2.username &&
                b1.uuid == b2.uuid
    }

}