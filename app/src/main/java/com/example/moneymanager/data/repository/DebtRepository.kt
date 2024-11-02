package com.example.moneymanager.data.repository

import com.example.moneymanager.data.dao.DebtDao
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.DebtDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DebtRepository {
    fun getDebtsByAccountId(userId: Long) : Flow<List<DebtDetail>>
    suspend fun insertDebt(debt: Debt) : Long
    suspend fun editDebt(debt: Debt)
    suspend fun deleteDebt(debtId: Long)
    suspend fun deleteDebt(debt: Debt)
}

class DebtRepositoryImpl @Inject constructor(
    private val debtDao: DebtDao
) : DebtRepository {
    override fun getDebtsByAccountId(userId: Long): Flow<List<DebtDetail>> {
        return debtDao.getDebtsByAccountId(userId)
    }

    override suspend fun insertDebt(debt: Debt): Long {
        return debtDao.insertDebt(debt)
    }

    override suspend fun editDebt(debt: Debt) {
        debtDao.editDebt(debt)
    }

    override suspend fun deleteDebt(debtId: Long) {
        debtDao.deleteDebt(debtId)
    }

    override suspend fun deleteDebt(debt: Debt) {
        debtDao.deleteDebt(debt)
    }
}