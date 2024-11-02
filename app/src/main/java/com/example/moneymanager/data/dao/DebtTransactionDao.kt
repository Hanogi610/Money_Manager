package com.example.moneymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneymanager.data.model.entity.DebtTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtTransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDebtTransaction(debtTransaction: DebtTransaction) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editDebtTransaction(debtTransaction: DebtTransaction)

    @Query("SELECT * FROM debt_transaction WHERE debt_id = :debtId")
    fun getDebtTransactionsByDebtId(debtId: Long) : Flow<List<DebtTransaction>>

    @Query("SELECT * FROM debt_transaction WHERE account_id = :accountId")
    fun getDebtTransactionsByAccountId(accountId: Long) : Flow<List<DebtTransaction>>

    @Query("DELETE FROM debt_transaction WHERE id = :debtTransactionId")
    suspend fun deleteDebtTransaction(debtTransactionId: Long)

    @Delete
    suspend fun deleteDebtTransaction(debtTransaction: DebtTransaction)
}