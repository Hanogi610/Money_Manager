package com.example.moneymanager.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.moneymanager.core.DebtTypeConverter
import com.example.moneymanager.data.model.Transaction
import com.example.moneymanager.data.model.entity.enums.DebtType

@Entity(
    tableName = "debt", foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["account_id"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Wallet::class,
        parentColumns = ["id"],
        childColumns = ["wallet_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Debt(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    override val name: String,
    override val amount: Double,
    @ColumnInfo(name = "account_id") override val accountId: Long,
    @TypeConverters(DebtTypeConverter::class) val type: DebtType,
    val date: Long,
    val time: Long,
    val description: String,
    @ColumnInfo(name = "wallet_id") val walletId: Long
) : Transaction(id, name, amount, accountId)

data class DebtDetail(
    @Embedded val debt: Debt,
    @Relation(
        parentColumn = "id",
        entityColumn = "debt_id"
    )
    val transactions: List<DebtTransaction>
)
