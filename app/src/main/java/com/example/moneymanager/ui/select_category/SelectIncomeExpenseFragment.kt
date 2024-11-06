package com.example.moneymanager.ui.select_category

import SelectIncomeExpenseAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanager.data.model.CategoryData
import com.example.moneymanager.databinding.FragmentSelectIncomeExpenseBinding
import com.example.moneymanager.ui.add.AddViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class SelectIncomeExpenseFragment : Fragment() {
    private var _binding: FragmentSelectIncomeExpenseBinding? = null
    private val binding get() = _binding!!
    lateinit var selectIncomeExpenseAdapter : SelectIncomeExpenseAdapter;
    private val viewModel: AddViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectIncomeExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showData()
        observeData()
    }

    fun observeData(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryListExpense.collect() { value ->
                    selectIncomeExpenseAdapter.updateData(value)
                }
            }
        }
    }

    fun clickRadioButtonIconCategory(categoryData: CategoryData.Category){
        if(categoryData.isCheck == false) {
            viewModel.setOneCategory(categoryData)
            var bundle = bundleOf("id" to categoryData.id)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("categoryBundle", bundle)
            findNavController().popBackStack()
        }
    }

    fun showData(){
        selectIncomeExpenseAdapter = SelectIncomeExpenseAdapter(viewModel.getCategoryListExpense(),:: clickRadioButtonIconCategory )
        binding.vpCategory.adapter = selectIncomeExpenseAdapter
        binding.vpCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
