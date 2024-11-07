package com.example.moneymanager.ui.wallet_screen.debt_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.R
import com.example.moneymanager.core.toFormattedDateString
import com.example.moneymanager.data.model.entity.DebtTransaction
import com.example.moneymanager.data.model.entity.enums.DebtActionType
import com.example.moneymanager.databinding.FragmentDebtDetailBinding
import com.example.moneymanager.ui.MainViewModel
import com.example.moneymanager.ui.wallet_screen.adapter.DebtTransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DebtDetailFragment : Fragment() {

    private var _binding: FragmentDebtDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DebtDetailViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var debtTransactionAdapter: DebtTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebtDetailBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
            mainViewModel.setCurrentDebt(null)
        }
        viewModel.getDebtDetails(mainViewModel.currentDebt.value!!.id)
        val currentCurrencySymbol =
            getString(mainViewModel.currentAccount.value!!.account.currency.symbolRes)
        debtTransactionAdapter = DebtTransactionAdapter(
            requireContext(), currentCurrencySymbol, mainViewModel.currentAccount.value!!.wallets
        )
        binding.debtTransactionRv.adapter = debtTransactionAdapter
        binding.debtTransactionRv.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.debtInfo.collect {
                    it?.let {
                        binding.apply {
                            nameTv.text = it.debt.name
                            descriptionTv.text = it.debt.description
                            val paidAmount =
                                it.transactions.filter { action -> action.action == DebtActionType.REPAYMENT }
                                    .sumOf { transaction -> transaction.amount }
                            paidAmountTv.text =
                                getString(R.string.money_amount, currentCurrencySymbol, paidAmount)
                            leftAmountTv.text = getString(
                                R.string.money_amount,
                                currentCurrencySymbol,
                                it.debt.amount - paidAmount
                            )
                            progressBar.progress =
                                ((paidAmount.toFloat() / it.debt.amount) * 100).toInt()
                            amountValue.text = getString(
                                R.string.money_amount, currentCurrencySymbol, it.debt.amount
                            )
                            dateValue.text = it.debt.date.toFormattedDateString()
                            walletValue.text =
                                mainViewModel.currentAccount.value!!.wallets.find { wallet -> wallet.id == it.debt.walletId }?.name
                        }
                    }
                    debtTransactionAdapter.updateItems(groupTransactionsByDate(it?.transactions ?: emptyList()))

                }
            }
        }

        binding.editButton.setOnClickListener {
            // Navigate to EditDebtFragment
            mainViewModel.setCurrentDebt(viewModel.debtInfo.value!!.debt)
            findNavController().navigate(R.id.action_debtDetailFragment_to_addDebtFragment)
        }
        binding.deleteButton.setOnClickListener {
            // Delete debt
            viewModel.deleteDebt(mainViewModel.currentDebt.value!!.id)
            findNavController().popBackStack()
        }

        binding.addDebtAction.setOnClickListener {
            // Navigate to AddDebtActionFragment
            findNavController().navigate(R.id.action_debtDetailFragment_to_addDebtTransactionFragment)
        }

        return binding.root
    }

    private fun groupTransactionsByDate(transactions: List<DebtTransaction>): List<DebtListItem> {
        val groupedList = mutableListOf<DebtListItem>()
        var lastDate: String? = null

        for (transaction in transactions) {
            // Use toFormattedDateString to format the date
            val formattedDate = transaction.date.toFormattedDateString("dd MMM yyyy")

            if (formattedDate != lastDate) {
                groupedList.add(DebtListItem.DateHeader(formattedDate))
                lastDate = formattedDate
            }

            groupedList.add(DebtListItem.DebtTransactionItem(transaction))
        }

        return groupedList
    }
    companion object {
        private const val TAG = "DebtDetailFragment"
    }
}

sealed class DebtListItem {
    data class DateHeader(val date: String) : DebtListItem()
    data class DebtTransactionItem(val transaction: DebtTransaction) : DebtListItem()
}
