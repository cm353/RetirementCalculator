package com.example.retirementcalculator

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    val viewModel : RetirementViewModel by viewModels()
    lateinit var fmt : DateTimeFormatter


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(this)
        fmt = DateTimeFormat.forPattern(getString(R.string.birthdate_format))

        //Datepicker
        et_birthdate.setOnClickListener {
            DatepickerFragment().show(supportFragmentManager, DatepickerFragment.TAG)
        }

        viewModel.birthdate.observe(this@MainActivity, Observer {
            et_birthdate.setText( it.toString(fmt))
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(viewModel.result[0]!=-1 &&  viewModel.result[0]!=-2) {
            updateUI()
        }
    }

    private fun setRetirementAge() {
        if( rb_retire_with_67.isChecked) {
            viewModel.retirementAge = 67
            Log.d(TAG, "calc: retirement Age set to 67")
        } else if(rb_retire_with_65.isChecked) {
            viewModel.retirementAge = 65
            Log.d(TAG, "calc: retirement Age set to 65")
        }
    }

    private fun setName() {
        viewModel.name = et_firstname.text.toString()
    }

    private fun calculateTimeToRetirement() {
        viewModel.dif()
    }


    private fun updateUI(){
        if(!viewModel.name.equals("")) {
            if(viewModel.result[0] > 1 && viewModel.result[1] > 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result,viewModel.result[0].toString(),viewModel.result[1].toString())
                retirementDateText()
            }
            if(viewModel.result[0] > 1 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_1,viewModel.result[0].toString(), viewModel.result[1].toString())
                retirementDateText()}

            if(viewModel.result[0] == 1 && viewModel.result[1] > 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_2,viewModel.result[0].toString(), viewModel.result[1].toString())
                retirementDateText()}

            if(viewModel.result[0] == 1 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_3,viewModel.result[0].toString(), viewModel.result[1].toString())
                retirementDateText()}
            if(viewModel.result[0] == 0 && viewModel.result[1] > 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_4, viewModel.result[1].toString())
                retirementDateText()}

            if(viewModel.result[0] == 0 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_5, viewModel.result[1].toString())
                retirementDateText()}

            if(viewModel.result[0] == 0 && viewModel.result[1] == 0 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_6)
                retirementDateText()}

            if(viewModel.result[0] == -2 && viewModel.result[1] == -2) {
                tv_name.text = ""
                Toast.makeText(this, getText(R.string.toast_dateOfBirth), Toast.LENGTH_SHORT).show()
                tv_result.text = ""
                tv_ret_date.text = ""
                tv_retdate_2.text = ""
            }
            if((viewModel.result[0] == -3 && viewModel.result[1] == -3)) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.retired)
                tv_ret_date.text = ""
                tv_retdate_2.text = ""
            }
        } else {
            Toast.makeText(this,getText(R.string.toast_name),Toast.LENGTH_SHORT).show()
        }
    }


    fun retirementDateText() {
        tv_ret_date.text = getString(R.string.retdate_text, viewModel.retirementDate.plusDays(1).toString(fmt))
        tv_retdate_2.text = getString(R.string.ret_date_2)
    }

    private fun hideKeyboard(v : View) {
        val  inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0)
    }

    fun calc(v: View) {
        setRetirementAge()
        setName()
        calculateTimeToRetirement()
        updateUI()
        hideKeyboard(v)
        }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.setDateOfBirth(dayOfMonth,month+1,year)
    }


}

