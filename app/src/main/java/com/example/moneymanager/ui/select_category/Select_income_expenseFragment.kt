package com.example.moneymanager.ui.select_category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanager.data.model.CategoryData
import com.example.moneymanager.databinding.FragmentSelectIncomeExpenseBinding

class SelectIncomeExpenseFragment : Fragment() {
    private var _binding: FragmentSelectIncomeExpenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectIncomeExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryData = CategoryData()
        val categoryListExpense = categoryData.readJsonCategory(requireContext(), " default_categories_expense.json")
        val categoryListIncome = categoryData.readJsonCategory(requireContext(), "default_categories_income.json")
        Log.i("listdata", "hello")
        Log.i("listdata", categoryListExpense.toString())
        Log.i("listdata", categoryListIncome.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
