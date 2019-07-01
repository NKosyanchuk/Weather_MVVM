package com.weather.weathermvvmapp.extensions

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.weather.weathermvvmapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun AppCompatActivity.loadFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(R.id.container, fragment)
    ft.commit()
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
}

fun getDateFromString(mills: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = mills
    return formatter.format(calendar.time)
}

fun Fragment.replaceFragment(newFragment: Fragment) {
    this.fragmentManager?.beginTransaction()
        ?.replace(R.id.container, newFragment)
        ?.addToBackStack(null)
        ?.commit()
}

fun Double.roundDoubleToString(): String {
    return this.roundToInt().toString()
}