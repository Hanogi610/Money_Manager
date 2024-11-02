package com.example.moneymanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moneymanager.data.dao.AccountDao
import com.example.moneymanager.data.dao.DebtDao
import com.example.moneymanager.data.dao.DebtTransactionDao
import com.example.moneymanager.data.dao.TransferDao
import com.example.moneymanager.data.dao.WalletDao
import com.example.moneymanager.data.model.entity.Account
import com.example.moneymanager.data.model.entity.Budget
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.DebtTransaction
import com.example.moneymanager.data.model.entity.DepositAndWithdraw
import com.example.moneymanager.data.model.entity.Goal
import com.example.moneymanager.data.model.entity.Transfer
import com.example.moneymanager.data.model.entity.Wallet

@Database(
    entities = [
        Account::class, Budget::class, DepositAndWithdraw::class, Goal::class,
        Transfer::class, Wallet::class, Debt::class, DebtTransaction::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
//    abstract fun budgetDao(): BudgetDao
    abstract fun debtDao(): DebtDao
    abstract fun debtTransactionDao(): DebtTransactionDao
//    abstract fun depositAndWithdrawDao(): DepositAndWithdrawDao
//    abstract fun goalDao(): GoalDao
    abstract fun transferDao(): TransferDao
//    abstract fun transferWalletDao(): TransferWalletDao
//    abstract fun typeMoneyDao(): TypeMoneyDao
    abstract fun walletDao(): WalletDao
}