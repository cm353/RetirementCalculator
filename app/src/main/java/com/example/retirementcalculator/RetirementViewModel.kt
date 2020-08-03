package com.example.retirementcalculator


import android.util.Log
import androidx.lifecycle.ViewModel
import org.joda.time.*


class RetirementViewModel : ViewModel() {

    companion object {
        private val TAG = RetirementViewModel::class.java.simpleName
    }

    lateinit var name : String
    var birthdate : DateTime = DateTime()
    private lateinit var retirementDate : DateTime
    var retirementAge = 65
    private val now : DateTime = DateTime.now()
    var result : IntArray = intArrayOf(-1, -1) // defaultinit

    fun setDateOfBirth(day:Int,month:Int,year: Int) {
        birthdate = DateTime(year,month,day,0,0,0)
    }

    private fun setYearOfRetirement() {
        retirementDate = birthdate.plusYears(retirementAge)
    }

    private fun calculateYearsToRetirement() : Int {
        val result = Interval(now, retirementDate).toPeriod().years
        Log.d(TAG, "calculateYearsToRetirement: $result")
        return result
    }

    private fun calculateDaysToRetirment(yearsToRetirement : Int) : Int {
        val dateinXXYears = now.plusYears(yearsToRetirement)
        val result = Period(dateinXXYears, retirementDate, PeriodType.dayTime()).days
        Log.d(TAG, "calculateDaysToRetirment: retDate $dateinXXYears")
        Log.d(TAG, "calculateDaysToRetirment:  $result")
        return result
    }

    fun dif()  {
        if(birthdate.compareTo(now) > 0) {
            Log.d(TAG, "dif: birthdate is in future: diff=${Period(birthdate, now).millis}")
            result = intArrayOf(-2,-2) // Errorcode: birtdate in future
        } else {
            Log.d(TAG, "Birthdate: $birthdate Current date: $now")
            setYearOfRetirement()
            if(retirementDate.compareTo(now) < 1) {
                result = intArrayOf(-3,-3) // Errorcode: person already retired
            } else {
                val yearsToRetirement = calculateYearsToRetirement()
                val daysToRetirement = calculateDaysToRetirment(yearsToRetirement)
                result = intArrayOf(yearsToRetirement, daysToRetirement)
            }
        }
    }


}
