package com.krachkovsky.cryptoapp.models

import com.google.gson.annotations.SerializedName

data class CoinInfo(
    @SerializedName("Name")
    val name: String
)