package com.example.moneymanager.ui.wallet_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.R
import com.example.moneymanager.data.model.entity.DebtDetail
import com.example.moneymanager.databinding.AddNewItemBinding
import com.example.moneymanager.databinding.DebtItemBinding

class DebtAdapter(
    private val context: Context,
    private val currentCurrencySymbol: String,
    private val onItemClick: (DebtDetail) -> Unit,
    private val onAddNewClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var debts: List<DebtDetail> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return if (position == debts.size) TYPE_ADD_NEW else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            DebtViewHolder(DebtItemBinding.inflate(inflater, parent, false))
        } else {
            AddDebtViewHolder(AddNewItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val debt = debts[position]
            (holder as DebtViewHolder).bind(debt)
        } else if (holder is AddDebtViewHolder) {
            // No binding required for the "Add Debt" button
        }
    }

    override fun getItemCount(): Int {
        return debts.size + 1
    }

    inner class AddDebtViewHolder(val binding: AddNewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.addName.text = context.getString(R.string.add_debt)
            binding.root.setOnClickListener { onAddNewClick() }
        }
    }

    inner class DebtViewHolder(val binding: DebtItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(debt: DebtDetail) {
            binding.debtName.text = debt.debt.name
            binding.debtDescription.text = debt.debt.description
            binding.debtAmount.text = context.getString(
                R.string.money_amount, currentCurrencySymbol, debt.debt.amount
            )
            binding.root.setOnClickListener { onItemClick(debt) }
        }
    }

    fun setDebts(debts: List<DebtDetail>) {
        val diffCallback = DebtDiffCallback(this.debts, debts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.debts = debts
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_ADD_NEW = 1
    }
}

class DebtDiffCallback(
    private val oldList: List<DebtDetail>, private val newList: List<DebtDetail>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].debt.id == newList[newItemPosition].debt.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
