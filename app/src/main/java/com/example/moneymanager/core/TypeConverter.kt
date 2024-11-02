package com.example.moneymanager.core

import androidx.room.TypeConverter
import com.example.moneymanager.data.model.entity.enums.Currency
import com.example.moneymanager.data.model.entity.enums.DebtActionType
import com.example.moneymanager.data.model.entity.enums.GoalInputType
import com.example.moneymanager.data.model.entity.enums.WalletType
import java.sql.Time
import java.util.Calendar
import java.util.Date

class WalletTypeConverter {

    @TypeConverter
    fun fromWalletType(walletType: WalletType): String {
        return walletType.name
    }

    @TypeConverter
    fun toWalletType(value: String): WalletType {
        return WalletType.valueOf(value)
    }
}

class CurrencyTypeConverter {
    @TypeConverter
    fun fromCurrency(currency: Currency): Int {
        return currency.id
    }

    @TypeConverter
    fun toCurrency(id: Int): Currency? {
        return Currency.fromId(id)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTime(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    fun timeToTimestamp(time: Time?): Long? {
        return time?.time
    }

    @TypeConverter
    fun getDayOfWeekString(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Chủ Nhật"
            Calendar.MONDAY -> "Thứ Hai"
            Calendar.TUESDAY -> "Thứ Ba"
            Calendar.WEDNESDAY -> "Thứ Tư"
            Calendar.THURSDAY -> "Thứ Năm"
            Calendar.FRIDAY -> "Thứ Sáu"
            Calendar.SATURDAY -> "Thứ Bảy"
            else -> "Không xác định"
        }
    }
}

class GoalInputTypeConverter {
    @TypeConverter
    fun fromGoalInputType(goalInputType: GoalInputType): String {
        return goalInputType.name
    }

    @TypeConverter
    fun toGoalInputType(value: String): GoalInputType {
        return GoalInputType.valueOf(value)
    }
}

class DebtActionTypeConverter {
    @TypeConverter
    fun fromDebtType(debtActionType: DebtActionType): String {
        return debtActionType.name
    }

    @TypeConverter
    fun toDebtType(value: String): DebtActionType {
        return DebtActionType.valueOf(value)
    }
}

class DebtTypeConverter {
    @TypeConverter
    fun fromDebtType(debtActionType: DebtActionType): String {
        return debtActionType.name
    }

    @TypeConverter
    fun toDebtType(value: String): DebtActionType {
        return DebtActionType.valueOf(value)
    }
}

