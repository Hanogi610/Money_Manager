package com.example.moneymanager.core

import android.content.Context
import androidx.annotation.ColorInt
import com.example.moneymanager.data.model.entity.enums.Currency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Convert Long to Date
fun Long.toDate(): Date {
    return Date(this)
}

// Convert Date to Long
fun Date.toTimestamp(): Long {
    return this.time
}

fun String.toDateTimestamp(format: String = "dd/MM/yyyy"): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = sdf.parse(this)
    return date?.time ?: 0L
}

// Extension function to convert time string to timestamp
fun String.toTimeTimestamp(format: String = "HH:mm"): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val time = sdf.parse(this)
    return time?.time ?: 0L
}

// Convert Long to formatted date string
fun Long.toFormattedDateString(format: String = "yyyy-MM-dd"): String {
    val date = this.toDate()
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(date)
}

// Convert Long to formatted time string
fun Long.toFormattedTimeString(format: String = "HH:mm"): String {
    val time = this.toDate()
    val timeFormatter = SimpleDateFormat(format, Locale.getDefault())
    return timeFormatter.format(time)
}

fun getCurrencyName(context: Context, currency: Currency): String {
    return context.getString(currency.nameRes)
}

fun getCurrencySymbol(context: Context, currency: Currency): String {
    return context.getString(currency.symbolRes)
}

object ColorHelper {
    // Define a map of color IDs to color values.
    private val colorMap = mapOf(
        "red" to 0xFFFF0000.toInt(),    // Color Red
        "blue" to 0xFF0000FF.toInt(),   // Color Blue
        "green" to 0xFF00FF00.toInt(),  // Color Green
        "yellow" to 0xFFFFFF00.toInt(), // Color Yellow
        "purple" to 0xFF800080.toInt()  // Color Purple
    )

    // Function to retrieve color by ID with a default color if the ID is not found
    @ColorInt
    fun getColorById(colorId: String): Int {
        return colorMap[colorId] ?: 0xFF000000.toInt() // Default to black if not found
    }

    // Function to retrieve color ID by its value (color code)
    fun getColorIdByValue(colorValue: Int): String? {
        return colorMap.entries.find { it.value == colorValue }?.key
    }

    // Optional: A function to retrieve all available color options as a list
    fun getAllColors(): List<Pair<String, Int>> {
        return colorMap.entries.map { it.key to it.value }
    }
}

