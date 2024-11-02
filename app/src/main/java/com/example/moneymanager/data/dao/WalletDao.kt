package com.example.moneymanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneymanager.data.model.entity.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWallet(wallet: Wallet) : Long

    @Query("SELECT * FROM wallet where account_id = :userId")
    fun getWalletsByUserId(userId: Long) : Flow<List<Wallet>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editWallet(wallet: Wallet)

    @Query("DELETE FROM wallet WHERE id = :walletId")
    suspend fun deleteWallet(walletId: Long)

    @Delete
    suspend fun deleteWallet(wallet: Wallet)
}