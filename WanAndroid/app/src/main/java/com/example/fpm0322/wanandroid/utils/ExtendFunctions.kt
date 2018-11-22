package com.example.fpm0322.wanandroid.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONException
import org.json.JSONArray
import org.json.JSONObject

var LINE_SEPARATOR = System.getProperty("line.separator")
/**
 * 提示
 */
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun printLine(tag:String, isTop:Boolean ) {
    if (isTop) {
        Log.e(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
    } else {
        Log.e(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }
}

fun printJson(tag: String, msg: String, headString: String) {

    var message: String

    try {
        if (msg.startsWith("{")) {
            val jsonObject = JSONObject(msg)
            message = jsonObject.toString(4)//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
        } else if (msg.startsWith("[")) {
            val jsonArray = JSONArray(msg)
            message = jsonArray.toString(4)
        } else {
            message = msg
        }
    } catch (e: JSONException) {
        message = msg
    }

    printLine(tag, true)
    message = headString + LINE_SEPARATOR + message
    val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    for (line in lines) {
        Log.e(tag, "║ $line")
    }
    printLine(tag, false)
}


