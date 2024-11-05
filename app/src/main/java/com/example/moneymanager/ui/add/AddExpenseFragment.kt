package com.example.moneymanager.ui.add

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.moneymanager.R
import com.example.moneymanager.data.model.entity.AddTransfer
import com.example.moneymanager.databinding.FragmentAddExpenseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddViewModel by activityViewModels()

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        Log.d("nang", "null() called with: bitmap = $bitmap")
        bitmap?.let {
            viewModel.setBitmap(it)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateDateTime()
        navGraph()
        pickDate()
        pickTime()
        selectImage()
        observe()
        saveTranfer()
        selectWallet()
        selectCategory()
        back()
    }

    fun back(){
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    fun selectCategory(){
        binding.etCategory.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.selectIncomeExpenseFragment)
        }
    }

    fun selectWallet(){
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.wallet_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spWallet.adapter = adapter
        }
    }

    fun saveTranfer() {
        binding.tvSave.setOnClickListener {
            val amountText = binding.etAmount.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDouble()
                val description = binding.etDescription.text.toString()
                val time = binding.etTime.text.toString()
                val date = binding.etDate.text.toString()
                val getbitmap = viewModel.getBitmap()
                var linkimg = viewModel.saveDrawableToAppStorage(requireContext(), getbitmap)
                if(linkimg == null){
                    linkimg = ""
                }
                val type_icon_category = binding.etCategory.text.toString()
                val typeOfExpenditure = "Expense"
                val toWallet = 0
                val fromWallet = 1
                val typeColor = ""
                val transfer = AddTransfer(
                    amount,
                    description,
                    typeOfExpenditure,
                    toWallet.toLong(),
                    fromWallet.toLong(),
                    linkimg,
                    date,
                    time,
                    type_icon_category,
                    0.0
                )
                viewModel.saveIncomeAndExpense(transfer)
            } else {
                Log.e("AddExpenseFragment", "Amount is empty")
            }
        }
    }

    fun navGraph(){
        val controller_nav =findNavController()
        binding.tvIncome.setOnClickListener {
            controller_nav.navigate(R.id.addIncomeFragment)
        }
        binding.tvTransfer.setOnClickListener{
            controller_nav.navigate(R.id.addTranferFragment)
        }
    }

    fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedTime.collect { time ->
                    binding.etTime.setText(time)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.collect { date ->
                    binding.etDate.setText(date)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentDateTime.collect { dateTime ->
                    binding.etDate.setText(dateTime.first)
                    binding.etTime.setText(dateTime.second)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageUri.collect { bitmap ->
                    binding.ivImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    fun selectImage(){
        binding.ivMemo.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setItems(R.array.select_camera_and_gallery) { _, which ->
                when (which) {
                    0 -> {
                        if (requireActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePictureLauncher.launch(null)
                        } else {
                            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    1 -> {
                    }
                }
            }
            builder.show()
        }
    }


    fun pickTime(){
        binding.etTime.setOnClickListener {
            viewModel.showTimePickerDialog(requireContext())
        }

    }


    fun pickDate(){
        binding.etDate.setOnClickListener {
            viewModel.showDatePickerDialog(requireContext())
        }
    }

    private fun saveData(){
        val amount = binding.etAmount.text.toString().toDouble()
        val description = binding.etDescription.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}