package com.example.moneymanager.ui.wallet_screen.add_debt_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.data.model.entity.DebtTransaction
import com.example.moneymanager.data.repository.DebtTransactionRepository
import com.example.moneymanager.di.AppDispatchers
import com.example.moneymanager.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDebtTransactionViewModel @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val debtTransactionRepository: DebtTransactionRepository,
) : ViewModel() {
    fun addDebtTransaction(debtTransaction: DebtTransaction) : Long{
        return if (debtTransaction.amount > 0) {
            var debtId: Long = -1
            viewModelScope.launch(ioDispatcher) {
                debtId = debtTransactionRepository.insertDebtTransaction(debtTransaction)
            }
            debtId
        } else {
            -1L
        }
    }
}