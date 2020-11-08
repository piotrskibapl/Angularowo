package pl.piotrskiba.angularowo.layouts

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R

class TimeAmountPickerView(context: Context) : LinearLayout(context) {

    @BindView(R.id.amount)
    lateinit var timeAmountEditText: EditText

    @BindView(R.id.timeunit)
    lateinit var timeUnitSpinner: Spinner

    init {
        inflate(context, R.layout.time_amount_picker_view, this)
        ButterKnife.bind(this)

        ArrayAdapter.createFromResource(
                context,
                R.array.time_units,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeUnitSpinner.adapter = adapter
        }
    }

    fun getTimeAmount(): Long {
        var amount = 0L
        if (timeAmountEditText.text.isNotEmpty()) {
            amount = timeAmountEditText.text.toString().toLong()
        }
        val multiplier = when (timeUnitSpinner.selectedItemPosition) {
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