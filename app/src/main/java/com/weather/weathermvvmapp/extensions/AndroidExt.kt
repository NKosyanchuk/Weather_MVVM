package com.weather.weathermvvmapp.extensions

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.weather.weathermvvmapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
}

fun getDateFromString(mills: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        timeInMillis = mills
    }
    return formatter.format(calendar.time)
}

fun Fragment.replaceFragment(newFragment: Fragment) {
    this.fragmentManager?.transaction {
        replace(R.id.container, newFragment)
        addToBackStack(null)
    }
}

fun Double.roundDoubleToString(): String {
    return this.roundToInt().toString()
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment) {
    supportFragmentManager.transaction {
        replace(R.id.container, fragment)
    }
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transaction {
        add(fragment, tag)
    }
}

private inline fun FragmentManager.transaction(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun Fragment.setupTitle(title: String) {
    activity?.title = title
}