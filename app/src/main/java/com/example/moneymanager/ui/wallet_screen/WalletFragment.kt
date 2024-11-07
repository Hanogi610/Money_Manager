package com.example.moneymanager.ui.wallet_screen

import android.graphics.Color
import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.R
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.DebtDetail
import com.example.moneymanager.data.model.entity.Wallet
import com.example.moneymanager.databinding.FragmentWalletBinding
import com.example.moneymanager.ui.MainViewModel
import com.example.moneymanager.ui.wallet_screen.adapter.DebtAdapter
import com.example.moneymanager.ui.wallet_screen.adapter.WalletAdapter
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WalletViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var walletAdapter: WalletAdapter
    private lateinit var debtAdapter: DebtAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(inflater, container, false)

        val currentCurrency = mainViewModel.currentAccount.value!!.account.currency
        val currencySymbol = getString(currentCurrency.symbolRes)

        walletAdapter = WalletAdapter(requireContext(), currencySymbol, ::onWalletItemClick, ::onAddWalletClick)
        binding.walletRecyclerView.adapter = walletAdapter
        binding.walletRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        debtAdapter = DebtAdapter(requireContext(), currencySymbol, ::onDebtItemClick, ::onAddDebtClick)
        binding.debtRecyclerView.adapter = debtAdapter
        binding.debtRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.currentAccount.value?.account?.id?.let {
            viewModel.getWallets(it)
            viewModel.getDebts(it)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.wallets.collect { wallets ->
                    walletAdapter.setWallets(wallets)
                    binding.managerTextView.text = getString(R.string.manager, wallets.size)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.debts.collect { debts ->
                    debtAdapter.setDebts(debts)
                    binding.debtManagerTextView.text = getString(R.string.manager, debts.size)
                }
            }
        }

//        binding.pieChart.slices = listOf(
//            PieChart.Slice(0.2f, Color.BLUE),
//            PieChart.Slice(0.4f, Color.MAGENTA),
//            PieChart.Slice(0.3f, Color.YELLOW),
//            PieChart.Slice(0.1f, Color.CYAN)
//        )

        return binding.root
    }

    private fun onDebtItemClick(debt: Debt) {
        mainViewModel.setCurrentDebt(debt)
        findNavController().navigate(R.id.action_mainFragment_to_debtDetailFragment)
    }

    private fun onAddDebtClick() {
        findNavController().navigate(R.id.action_mainFragment_to_addDebtFragment)
    }

    private fun onAddWalletClick() {
        TODO("Not yet implemented")
    }

    private fun onWalletItemClick(wallet: Wallet) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}