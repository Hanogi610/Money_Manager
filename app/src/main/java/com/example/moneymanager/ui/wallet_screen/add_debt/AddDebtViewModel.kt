package com.example.moneymanager.ui.wallet_screen.add_debt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.repository.DebtRepository
import com.example.moneymanager.di.AppDispatchers
import com.example.moneymanager.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDebtViewModel @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val debtRepository: DebtRepository
) : ViewModel() {
    fun addDebt(debt: Debt): Long {
        return if (debt.name.isNotEmpty() && debt.amount > 0 && debt.description.isNotEmpty()) {
            var debtId: Long = -1
            viewModelScope.launch(ioDispatcher) {
                debtId = debtRepository.insertDebt(debt)
            }
            debtId
        } else {
            -1L
        }
    }

    fun editDebt(debt: Debt) {
        viewModelScope.launch(ioDispatcher) {
            debtRepository.editDebt(debt)
        }
    }
}