package com.example.moneymanager.ui.wallet_screen.add_debt_transaction

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moneymanager.R
import com.example.moneymanager.core.toDateTimestamp
import com.example.moneymanager.core.toTimeTimestamp
import com.example.moneymanager.data.model.entity.DebtTransaction
import com.example.moneymanager.data.model.entity.enums.DebtActionType
import com.example.moneymanager.data.model.entity.enums.DebtType
import com.example.moneymanager.databinding.FragmentAddDebtTransactionBinding
import com.example.moneymanager.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddDebtTransactionFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: AddDebtTransactionViewModel by viewModels()
    private var _binding: FragmentAddDebtTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDebtTransactionBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val debtAction = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            DebtActionType.entries.map { it.name }
        )
        binding.spinnerActionType.adapter = debtAction
        binding.spinnerActionType.setSelection(0) // Set default selection as first item

        // Set initial date and time to current date and time
        val calendar = Calendar.getInstance()
        binding.etDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        binding.etTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

        binding.etDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, dayOfMonth)
                    binding.etDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        binding.etTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    binding.etTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.accounts.collect { accounts ->
                    val wallets = accounts.flatMap { it.wallets }
                    val walletNames = wallets.map { it.name }
                    val walletAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        walletNames
                    )
                    binding.spinnerWallet.adapter = walletAdapter
                    binding.spinnerWallet.setSelection(0) // Set default selection as first item
                }
            }
        }

        // Add TextWatchers to EditTexts
        binding.etName.addTextChangedListener(textWatcher)
        binding.etAmount.addTextChangedListener(textWatcher)
        binding.etDate.addTextChangedListener(textWatcher)
        binding.etTime.addTextChangedListener(textWatcher)

        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString().toDouble()
            val date = binding.etDate.text.toString().toDateTimestamp()
            val time = binding.etTime.text.toString().toTimeTimestamp()
            val wallet = mainViewModel.accounts.value.flatMap { it.wallets }.find { it.name == binding.spinnerWallet.selectedItem.toString() }!!.id
            val actionType = DebtActionType.valueOf(binding.spinnerActionType.selectedItem.toString())
            val debtTransaction = DebtTransaction(
                name = name,
                amount = amount,
                date = date,
                time = time,
                walletId = wallet,
                action = actionType,
                accountId = mainViewModel.currentAccount.value!!.account.id,
                debtId = mainViewModel.currentDebt.value!!.id
            )
            viewModel.addDebtTransaction(debtTransaction)
            findNavController().popBackStack()
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            checkSaveButton()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun checkSaveButton() {
        val name = binding.etName.text.toString()
        val amount = binding.etAmount.text.toString().toDoubleOrNull()
        val date = binding.etDate.text.toString()
        val time = binding.etTime.text.toString()
        binding.saveButton.isEnabled = amount != null && name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}