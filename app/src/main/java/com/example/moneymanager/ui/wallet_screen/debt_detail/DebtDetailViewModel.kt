package com.example.moneymanager.ui.wallet_screen.debt_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.DebtDetail
import com.example.moneymanager.data.repository.DebtRepository
import com.example.moneymanager.di.AppDispatchers
import com.example.moneymanager.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtDetailViewModel @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val debtRepository: DebtRepository
) : ViewModel() {
    private val _debtInfo = MutableStateFlow<DebtDetail?>(null)
    val debtInfo: StateFlow<DebtDetail?> get() = _debtInfo

    fun getDebtDetails(debtId: Long) {
        viewModelScope.launch(ioDispatcher) {
            debtRepository.getDebtDetailsByDebtId(debtId).collect {
                Log.d(TAG, "getDebtDetails: $it")
                _debtInfo.value = it
            }
        }
    }

    fun deleteDebt(debtId: Long) {
        viewModelScope.launch(ioDispatcher) {
            debtRepository.deleteDebt(debtId)
        }
    }

    companion object {
        private const val TAG = "DebtDetailViewModel"
    }
}