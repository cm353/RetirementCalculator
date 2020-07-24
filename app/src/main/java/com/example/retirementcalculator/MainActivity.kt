package com.example.retirementcalculator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    val viewModel : RetirementViewModel by viewModels()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(viewModel.result[0]!=-1 &&  viewModel.result[0]!=-2) {
            updateUI()
            dpResult.updateDate(viewModel.birthdate.year, viewModel.birthdate.monthOfYear-1,viewModel.birthdate.dayOfMonth)
        }
    }

    private fun setRetirementAge() {
        if( sw_retirement.isChecked) {
            viewModel.retirementAge = 67
            Log.d(TAG, "calc: retirement Age set to 67")
        } else {
            viewModel.retirementAge = 65
            Log.d(TAG, "calc: retirement Age set to 65")
        }
    }

    private fun setDateOfBirth() {
        viewModel.setDateOfBirth(dpResult.dayOfMonth,dpResult.month+1,dpResult.year )
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
            }
            if(viewModel.result[0] > 1 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_1,viewModel.result[0].toString(), viewModel.result[1].toString()) }
            if(viewModel.result[0] == 1 && viewModel.result[1] > 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_2,viewModel.result[0].toString(), viewModel.result[1].toString()) }
            if(viewModel.result[0] == 1 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_3,viewModel.result[0].toString(), viewModel.result[1].toString()) }
            if(viewModel.result[0] == 0 && viewModel.result[1] > 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_4, viewModel.result[1].toString()) }
            if(viewModel.result[0] == 0 && viewModel.result[1] == 1 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_5, viewModel.result[1].toString()) }
            if(viewModel.result[0] == 0 && viewModel.result[1] == 0 ) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.tv_result_6) }
            if(viewModel.result[0] == -2 && viewModel.result[1] == -2) {
                tv_name.text = ""
                Toast.makeText(this, getText(R.string.toast_dateOfBirth), Toast.LENGTH_SHORT).show()
                tv_result.text = ""
            }
            if((viewModel.result[0] == -3 && viewModel.result[1] == -3)) {
                tv_name.text = getString(R.string.tv_name, viewModel.name)
                tv_result.text = getString(R.string.retired)
            }
        } else {
            Toast.makeText(this,getText(R.string.toast_name),Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard(v : View) {
        val  inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0)
    }

    fun calc(v: View) {
        setDateOfBirth()
        setRetirementAge()
        setName()
        calculateTimeToRetirement()
        updateUI()
        hideKeyboard(v)
        }
}

