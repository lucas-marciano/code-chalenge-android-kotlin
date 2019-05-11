package com.arctouch.codechallenge.util

import java.text.SimpleDateFormat
import java.util.*


fun String.convertToPtBrDateFormat(): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this)
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
}
