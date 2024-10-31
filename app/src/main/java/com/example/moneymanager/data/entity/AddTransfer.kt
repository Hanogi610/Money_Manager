package com.example.moneymanager.data.entity

data class AddTransfer(
    val amount : Double,
    val description: String,
    val typeOfExpenditure : String,
    val toWallet: Long,
    val fromWallet: Long,
    val linkImg: String,
    val transferDate: String,
    val transferTime: String,
    val typeDebt: String,
    val typeIconCategory: String,
    val typeColor: String,
    val typeIconWallet: String,
    val fee: Double,
) {
    constructor() : this(0.0, "", "", 0L, 0L, "", "", "", "", "", "", "", 0.0)
}