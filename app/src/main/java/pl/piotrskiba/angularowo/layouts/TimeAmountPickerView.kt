package pl.piotrskiba.angularowo.layouts

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.databinding.TimeAmountPickerViewBinding

class TimeAmountPickerView(context: Context) : LinearLayout(context) {

    private val binding: TimeAmountPickerViewBinding

    init {
        inflate(context, R.layout.time_amount_picker_view, this)
        binding = TimeAmountPickerViewBinding.bind(this)

        ArrayAdapter.createFromResource(
            context,
            R.array.time_units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timeunit.adapter = adapter
        }
    }

    fun getTimeAmount(): Long {
        var amount = 0L
        if (binding.amount.text.isNotEmpty()) {
            amount = binding.amount.text.toString().toLong()
        }
        val multiplier = when (binding.timeunit.selectedItemPosition) {
            1 -> 60
            2 -> 60 * 60
            3 -> 24 * 60 * 60
            4 -> 30 * 24 * 60 * 60
            5 -> 12 * 30 * 24 * 60 * 60
            else -> 1
        }
        return amount * multiplier
    }
}
