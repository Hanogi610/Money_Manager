package com.example.moneymanager.ui.wallet_screen.debt_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.moneymanager.databinding.FragmentDebtDetailBinding
import com.example.moneymanager.ui.MainViewModel

class DebtDetailFragment : Fragment() {

    private var _binding: FragmentDebtDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DebtDetailViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebtDetailBinding.inflate(inflater, container, false)



        return binding.root
    }
}