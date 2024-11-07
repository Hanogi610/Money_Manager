package com.example.moneymanager.ui.wallet_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.R
import com.example.moneymanager.core.toFormattedTimeString
import com.example.moneymanager.data.model.entity.DebtTransaction
import com.example.moneymanager.data.model.entity.Wallet
import com.example.moneymanager.databinding.DateHeaderItemBinding
import com.example.moneymanager.databinding.DebtTransactionItemBinding
import com.example.moneymanager.ui.wallet_screen.debt_detail.DebtListItem

class DebtTransactionAdapter(
    private val context: Context,
    private val currencySymbol: String,
    private val wallets: List<Wallet>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DATE_HEADER = 0
        private const val VIEW_TYPE_TRANSACTION_ITEM = 1
    }

    private var items: List<DebtListItem> = listOf()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DebtListItem.DateHeader -> VIEW_TYPE_DATE_HEADER
            is DebtListItem.DebtTransactionItem -> VIEW_TYPE_TRANSACTION_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATE_HEADER) {
            val binding = DateHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DateHeaderViewHolder(binding)
        } else {
            val binding = DebtTransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransactionViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DebtListItem.DateHeader -> (holder as DateHeaderViewHolder).bind(item)
            is DebtListItem.DebtTransactionItem -> (holder as TransactionViewHolder).bind(item.transaction)
        }
    }

    override fun getItemCount() = items.size

    inner class DateHeaderViewHolder(private val binding: DateHeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: DebtListItem.DateHeader) {
            binding.dateTextView.text = header.date
        }
    }

    inner class TransactionViewHolder(private val binding: DebtTransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: DebtTransaction) {
            binding.transactionTypeTextView.text = context.getString(R.string.debt_transaction_name, transaction.action.toString(), transaction.name)
            binding.transactionAmount.text = context.getString(R.string.money_amount, currencySymbol, transaction.amount)
            binding.transactionTime.text = transaction.time.toFormattedTimeString()
            binding.walletName.text = wallets.find { it.id == transaction.walletId }?.name
        }
    }

    // Method to update the items with DiffUtil
    fun updateItems(newItems: List<DebtListItem>) {
        val diffCallback = DebtListItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}


class DebtListItemDiffCallback(
    private val oldList: List<DebtListItem>,
    private val newList: List<DebtListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is DebtListItem.DateHeader && newItem is DebtListItem.DateHeader) {
            oldItem.date == newItem.date
        } else if (oldItem is DebtListItem.DebtTransactionItem && newItem is DebtListItem.DebtTransactionItem) {
            oldItem.transaction.id == newItem.transaction.id
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
