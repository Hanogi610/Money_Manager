package com.example.moneymanager.data.repository

import com.example.moneymanager.data.dao.WalletDao
import com.example.moneymanager.data.model.entity.Wallet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WalletRepository {
    fun getWalletsByUserId(userId: Long) : Flow<List<Wallet>>
    suspend fun insertWallet(wallet: Wallet) : Long
    suspend fun editWallet(wallet: Wallet)
    suspend fun deleteWallet(walletId: Long)
    suspend fun deleteWallet(wallet: Wallet)
}

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    override fun getWalletsByUserId(userId: Long): Flow<List<Wallet>> {
        return walletDao.getWalletsByUserId(userId)
    }

    override suspend fun insertWallet(wallet: Wallet): Long {
        return walletDao.insertWallet(wallet)
    }

    override suspend fun editWallet(wallet: Wallet) {
        walletDao.editWallet(wallet)
    }

    override suspend fun deleteWallet(walletId: Long) {
        walletDao.deleteWallet(walletId)
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet)
    }
}