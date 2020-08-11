package ru.unmei.app

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

fun AppCompatActivity.newActivity(activity: AppCompatActivity, finishLayout: Boolean) {
    val intent = Intent(this, activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    startActivity(intent)
    if (finishLayout) this.finish()
}

fun textToastShort(msg: Int, context: Context) {
    Toast.makeText(context, context.resources.getString(msg), Toast.LENGTH_SHORT).show()
}

fun textToastShort(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun textToastLong(msg: Int, context: Context) {
    Toast.makeText(context, context.resources.getString(msg), Toast.LENGTH_LONG).show()
}

fun textToastLong(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun onWebButtonClick(id: Int, url: String, view: View) {
    val webBtn = view.findViewById<View>(id)
    webBtn.setOnClickListener {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        webBtn.context.startActivity(intent)
    }
}

fun putStringData(key: String, value: String, context: Context) {
    val sp = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.putString(key, value)
    editor.apply()
}

fun deleteStringData(key: String, context: Context) {
    val sp = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.remove(key)
    editor.apply()
}

fun getStringData(context: Context): SharedPreferences {
    return context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
}

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
fun setTime(input: String): String {
    val tz = TimeZone.getTimeZone("Europe/Moscow")
    val df = SimpleDateFormat("dd.MM.YYYY, HH:mm:ss")
    df.timeZone = tz
    val ta = DateTimeFormatter.ISO_INSTANT.parse(input)
    val i = Instant.from(ta)
    val d = Date.from(i)
    return df.format(d)
}

fun setProgressBar(view: View): ProgressDialog {
    val mProgressDialog = ProgressDialog(view.context)
    mProgressDialog.isIndeterminate = true
    mProgressDialog.setMessage("Loading...")
    mProgressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK)
    return mProgressDialog
}