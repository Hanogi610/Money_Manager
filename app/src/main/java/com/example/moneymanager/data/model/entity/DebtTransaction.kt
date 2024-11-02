package com.example.moneymanager.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moneymanager.core.DebtActionTypeConverter
import com.example.moneymanager.data.model.Transaction
import com.example.moneymanager.data.model.entity.enums.DebtActionType

@Entity(
    tableName = "debt_transaction", foreignKeys = [ForeignKey(
        entity = Debt::class,
        parentColumns = ["id"],
        childColumns = ["debt_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Wallet::class,
        parentColumns = ["id"],
        childColumns = ["wallet_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["account_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DebtTransaction(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    override val name: String,
    @ColumnInfo(name = "account_id") override val accountId: Long,
    @ColumnInfo(name = "debt_id") val debtId: Long,
    @ColumnInfo(name = "wallet_id") val walletId: Long,
    override val amount: Double,
    @TypeConverters(DebtActionTypeConverter::class) val action: DebtActionType,
    val date: Long,
    val time: Long,
) : Transaction(id, name, amount, accountId)