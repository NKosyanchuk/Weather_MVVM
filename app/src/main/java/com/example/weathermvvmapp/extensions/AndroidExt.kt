package com.example.weathermvvmapp.extensions

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weathermvvmapp.R

fun AppCompatActivity.loadFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(R.id.container, fragment)
    ft.commit()
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}