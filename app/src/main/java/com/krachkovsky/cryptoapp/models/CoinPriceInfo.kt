package com.krachkovsky.cryptoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.krachkovsky.cryptoapp.util.Constants.BASE_IMAGE_URL
import com.krachkovsky.cryptoapp.util.convertTimestampToTime

@Entity(tableName = "full_price_list")
data class CoinPriceInfo(
    @SerializedName("CHANGEDAY")
    val changeDay: String,
    @PrimaryKey
    @SerializedName("FROMSYMBOL")
    val fromSymbol: String,
    @SerializedName("HIGH24HOUR")
    val highDay: String,
    @SerializedName("LOW24HOUR")
    val lowDay: String,
    @SerializedName("IMAGEURL")
    val imageURL: String,
    @SerializedName("LASTMARKET")
    val lastMarket: String,
    @SerializedName("LASTUPDATE")
    val lastUpdate: Long,
    @SerializedName("MARKET")
    val Market: String,
    @SerializedName("OPENDAY")
    val openDay: String,
    @SerializedName("PRICE")
    val price: String,
    @SerializedName("SUPPLY")
    val supply: String,
    @SerializedName("TOSYMBOL")
    val toSymbol: String,
) {

    fun getFormattedTime(): String {
        return convertTimestampToTime(lastUpdate)
    }

    fun getFullImageURL(): String {
        return BASE_IMAGE_URL + imageURL
    }
}