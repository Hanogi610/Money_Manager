package com.example.moneymanager.ui.wallet_screen.add_debt

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moneymanager.R
import com.example.moneymanager.core.ColorHelper
import com.example.moneymanager.core.toDateTimestamp
import com.example.moneymanager.core.toTimeTimestamp
import com.example.moneymanager.data.model.entity.Debt
import com.example.moneymanager.data.model.entity.enums.DebtType
import com.example.moneymanager.databinding.FragmentAddDebtBinding
import com.example.moneymanager.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddDebtFragment : Fragment() {

    private var _binding: FragmentAddDebtBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddDebtViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDebtBinding.inflate(inflater, container, false)

        binding.backArrowButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Set up the spinner with the enum values
        val debtTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            DebtType.entries.map { it.name }
        )
        binding.spinnerDebtType.adapter = debtTypeAdapter
        binding.spinnerDebtType.setSelection(0) // Set default value as first option

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
                    binding.walletSpinner.adapter = walletAdapter
                }
            }
        }

        // Set initial date and time to current date and time
        val calendar = Calendar.getInstance()
        binding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        binding.timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

        // Open date picker and convert date to long
        binding.dateTextView.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    checkFieldsForEmptyValues()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Open time picker and convert time to long
        binding.timeTextView.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    binding.timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                    checkFieldsForEmptyValues()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        // Set up the spinner with color options
        val colorOptions = resources.getStringArray(R.array.color_options)
        val colorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            colorOptions
        )
        binding.colorSpinner.adapter = colorAdapter
        binding.colorSpinner.setSelection(0) // Set default value as first option

        // Add TextWatchers to EditTexts
        binding.editTextName.addTextChangedListener(textWatcher)
        binding.editTextDescription.addTextChangedListener(textWatcher)
        binding.editTextAmount.addTextChangedListener(textWatcher)

        // Check if the currentDebt is null or not for adding/editing
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.currentDebt.collect { currentDebt: Debt? ->
                    if (currentDebt != null) {
                        // Editing an existing debt
                        populateFieldsWithDebtData(currentDebt)
                        binding.saveButton.setOnClickListener {
                            val updatedDebt = buildDebtFromFields(currentDebt.id) // Use currentDebt.id for update
                            viewModel.editDebt(updatedDebt)
                            findNavController().popBackStack()
                        }
                    } else {
                        // Adding a new debt
                        binding.saveButton.setOnClickListener {
                            val newDebt = buildDebtFromFields()
                            viewModel.addDebt(newDebt)
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            checkFieldsForEmptyValues()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun checkFieldsForEmptyValues() {
        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val amount = binding.editTextAmount.text.toString()
        val date = binding.dateTextView.text.toString()
        val time = binding.timeTextView.text.toString()

        binding.saveButton.isEnabled = name.isNotEmpty() && description.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
    }

    private fun populateFieldsWithDebtData(debt: Debt) {
        binding.editTextName.setText(debt.name)
        binding.editTextDescription.setText(debt.description)
        binding.editTextAmount.setText(getString(R.string.money_amount, "", debt.amount))
        binding.dateTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(debt.date)
        binding.timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(debt.time)
        binding.spinnerDebtType.setSelection(DebtType.valueOf(debt.type.name).ordinal)
        binding.walletSpinner.setSelection(getWalletIndex(debt.walletId))
        binding.colorSpinner.setSelection(getColorIndex(debt.colorId))
    }

    private fun getWalletIndex(walletId: Long): Int {
        val wallets = mainViewModel.currentAccount.value?.wallets.orEmpty()
        return wallets.indexOfFirst { it.id == walletId }.takeIf { it >= 0 } ?: 0
    }

    private fun getColorIndex(colorId: Int): Int {
        // Get the color name from the color value using the helper function
        val colorName = ColorHelper.getColorIdByValue(colorId)

        // If the color name is found, find its index in the color options array
        val colorOptions = resources.getStringArray(R.array.color_options)
        return colorName?.let {
            colorOptions.indexOf(it).takeIf { it >= 0 } ?: 0 // Return the index or default to 0
        } ?: 0 // Default to 0 if color name is not found
    }

    private fun buildDebtFromFields(debtId: Long? = null): Debt {
        val selectedWalletName = binding.walletSpinner.selectedItem.toString()
        val selectedWallet = mainViewModel.accounts.value.flatMap { it.wallets }.find { it.name == selectedWalletName }
        val selectedWalletId = selectedWallet?.id ?: 0 // Default to 0 if not found

        val selectedColorName = binding.colorSpinner.selectedItem.toString()
        val selectedColorId = ColorHelper.getColorById(selectedColorName)

        return Debt(
            id = debtId ?: 0, // Use existing debt id if editing, otherwise 0 for new debt
            name = binding.editTextName.text.toString(),
            amount = binding.editTextAmount.text.toString().replace(",",".").toDouble(),
            accountId = mainViewModel.currentAccount.value!!.account.id, // Set the account ID as needed
            type = DebtType.valueOf(binding.spinnerDebtType.selectedItem.toString()),
            date = binding.dateTextView.text.toString().toDateTimestamp(),
            time = binding.timeTextView.text.toString().toTimeTimestamp(),
            description = binding.editTextDescription.text.toString(),
            walletId = selectedWalletId, // Set the wallet ID
            colorId = selectedColorId // Set the color ID
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}