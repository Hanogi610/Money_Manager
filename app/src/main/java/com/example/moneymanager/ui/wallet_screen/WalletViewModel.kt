package com.example.moneymanager.ui.wallet_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.Wallet
import com.example.moneymanager.data.repository.DebtRepository
import com.example.moneymanager.data.repository.WalletRepository
import com.example.moneymanager.di.AppDispatchers
import com.example.moneymanager.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val walletRepository: WalletRepository,
    private val debtRepository: DebtRepository,
) : ViewModel() {

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> get() = _wallets

    private val _debts = MutableStateFlow<List<Debt>>(emptyList())
    val debts: StateFlow<List<Debt>> get() = _debts

    fun getWallets(accountId: Long) {
        viewModelScope.launch {
            walletRepository.getWalletsByUserId(accountId).collect {
                _wallets.value = it
            }
        }
    }

    fun getDebts(accountId: Long) {
        viewModelScope.launch {
            debtRepository.getDebtsByAccountId(accountId).collect {
                _debts.value = it
            }
        }
    }
}