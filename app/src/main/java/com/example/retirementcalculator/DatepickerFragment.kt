package com.example.retirementcalculator

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import java.util.*


class DatepickerFragment : DialogFragment() {

    var listener : DatePickerDialog.OnDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is DatePickerDialog.OnDateSetListener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]+1
        val day = calendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(activity!!,listener,year,month,day)
    }

    companion object {
        val TAG : String? = DatepickerFragment::class.java.simpleName
    }
}