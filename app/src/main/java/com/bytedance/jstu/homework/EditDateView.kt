package com.bytedance.jstu.homework

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.autofill.AutofillValue
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.util.*

class EditDateView(context :Context, attrs:AttributeSet) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    public var editDateMillis: Long = 0


    override fun getAutofillType(): Int {
        return AUTOFILL_TYPE_DATE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAutofillValue(): AutofillValue? {
        return AutofillValue.forDate(editDateMillis)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun autofill(value: AutofillValue?) {
        value?.let {
            if (value.isDate)
                editDateMillis = value.dateValue
        }

        val dateMillis : Long? = value?.dateValue?.toLong()
        this.text?.clear()
        this.text?.append(DateFormat.getDateInstance().format(Date(dateMillis?:0)))
    }
}