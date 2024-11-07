package com.example.moneymanager.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.data.model.CategoryData
import com.example.moneymanager.data.model.entity.AddTransfer
import com.example.moneymanager.data.model.entity.Transfer
import com.example.moneymanager.data.repository.TransferRepository
import com.example.moneymanager.di.AppDispatchers
import com.example.moneymanager.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TransferRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
)  : ViewModel() {
    private val _categoryListExpense = MutableStateFlow<List<CategoryData.Category>>(emptyList())
    val categoryListExpense: StateFlow<List<CategoryData.Category>> get() = _categoryListExpense

    private val _categoryListIncome = MutableStateFlow<List<CategoryData.Category>>(emptyList())
    val categoryListIncome: StateFlow<List<CategoryData.Category>> get() = _categoryListIncome

    private val _addTransfer = MutableStateFlow(AddTransfer())
    val addTransfer: StateFlow<AddTransfer> get() = _addTransfer

    private val _selectedDate = MutableStateFlow<String>("")
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _selectedTime = MutableStateFlow<String>("")
    val selectedTime: StateFlow<String> get() = _selectedTime

    private val _currentDateTime = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val currentDateTime: StateFlow<Pair<String, String>> get() = _currentDateTime

    private val _imageUri = MutableStateFlow<Bitmap?>(null)
    val imageUri: StateFlow<Bitmap?> get() = _imageUri

    fun setCategoryListExpense(list: List<CategoryData.Category>) {
        _categoryListExpense.value = list
    }
    fun setCategoryListIncome(list: List<CategoryData.Category>) {
        _categoryListIncome.value = list
    }

    fun getCategoryListExpense(): List<CategoryData.Category> {
        return _categoryListExpense.value
    }

    fun getOneCategoryExpense(idCategory : Int): CategoryData.Category? {
        return _categoryListExpense.value.find { it.id == idCategory }
    }

    fun getOneCategoryIncome(idCategory : Int): CategoryData.Category? {
        return _categoryListIncome.value.find { it.id == idCategory }
    }

    fun setOneCategoryExpense(category: CategoryData.Category) {
        val updatedList = _categoryListExpense.value.map {
            if (it.id == category.id) {
                it.copy(isCheck = true)
            } else {
                it.copy(isCheck = false)
            }
        }
        _categoryListExpense.value = updatedList
    }

    fun setOneCategoryIcome(category: CategoryData.Category) {
        val updatedList = _categoryListIncome.value.map {
            if (it.id == category.id) {
                it.copy(isCheck = true)
            } else {
                it.copy(isCheck = false)
            }
        }
        _categoryListIncome.value = updatedList
    }

    fun getCategoryListIncome(): List<CategoryData.Category> {
        return _categoryListIncome.value
    }

    fun setBitmap(bitmap: Bitmap) {
        _imageUri.value = bitmap
    }

    fun getBitmap(): Bitmap? {
        return _imageUri.value
    }

    fun saveDrawableToAppStorage(context: Context, bitmap : Bitmap?): String? {
        if (bitmap == null) {
            return null
        }
        val output = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val directory = File(output, "image")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val filename = "IMG_${System.currentTimeMillis()}.png"
        val file = File(directory, filename)
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }



    fun showDatePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            _selectedDate.value = selectedDate
        }, year, month, day)
        datePickerDialog.show()
    }

    fun showTimePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            val selectedTime = "$selectedHour:$selectedMinute"
            _selectedTime.value = selectedTime
        }, hour, minute, true)
        timePickerDialog.show()
    }

    fun updateDateTime() {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        _currentDateTime.value = Pair(dateFormat.format(currentDate), timeFormat.format(currentDate))
    }

    fun saveIncomeAndExpense(addTransfer: AddTransfer) {
        viewModelScope.launch(ioDispatcher) {
            val newAddTransfer = addTransfer
            if (newAddTransfer.amount > 0) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val transferDate = dateFormat.parse(newAddTransfer.transferDate)
                val transferTime = timeFormat.parse(newAddTransfer.transferTime)
                Log.i("AddViewModel", "transferDate: $addTransfer")
                repository.insertTransfer(
                    Transfer(
                        fromWallet = newAddTransfer.fromWallet,
                        toWallet = newAddTransfer.toWallet,
                        amount = newAddTransfer.amount,
                        fee = newAddTransfer.fee,
                        description = newAddTransfer.description,
                        linkImg = newAddTransfer.linkImg,
                        transferDate = transferDate!!.time,
                        transferTime = transferTime!!.time,
                        typeOfExpenditure = newAddTransfer.typeOfExpenditure,
                        typeIconCategory = newAddTransfer.typeIconCategory,)
                )
            }
        }
    }

    override fun onCleared(){
        super.onCleared()
    }

}
