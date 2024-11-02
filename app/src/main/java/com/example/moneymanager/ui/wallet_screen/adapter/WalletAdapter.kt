package com.example.moneymanager.ui.wallet_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanager.R
import com.example.moneymanager.data.model.entity.Wallet
import com.example.moneymanager.databinding.AddNewWalletItemBinding
import com.example.moneymanager.databinding.WalletItemBinding

class WalletAdapter(
    private val context: Context,
    private val currentCurrencySymbol: String,
    private val onWalletItemClick: (Wallet) -> Unit,
    private val onAddWalletClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var wallets: List<Wallet> = emptyList()

    inner class WalletViewHolder(private val binding: WalletItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallet: Wallet) {
            binding.walletName.text = wallet.name
            binding.walletBalance.text = context.getString(
                R.string.money_amount, currentCurrencySymbol, wallet.amount
            )
            binding.root.setOnClickListener { onWalletItemClick(wallet) }
        }
    }

    inner class AddWalletViewHolder(binding: AddNewWalletItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onAddWalletClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_WALLET) {
            WalletViewHolder(WalletItemBinding.inflate(inflater, parent, false))
        } else {
            AddWalletViewHolder(AddNewWalletItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        // Add one more item for the "Add Wallet" view
        return wallets.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_WALLET) {
            val wallet = wallets[position]
            (holder as WalletViewHolder).bind(wallet)
        } else if (holder is AddWalletViewHolder) {
            // No binding required for the "Add Wallet" button
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Show the "Add Wallet" view as the last item
        return if (position == wallets.size) TYPE_ADD_WALLET else TYPE_WALLET
    }

    fun setWallets(newWallets: List<Wallet>) {
        val diffCallback = WalletDiffCallback(wallets, newWallets)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        wallets = newWallets
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        private const val TYPE_WALLET = 0
        private const val TYPE_ADD_WALLET = 1
    }
}

class WalletDiffCallback(
    private val oldList: List<Wallet>,
    private val newList: List<Wallet>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
