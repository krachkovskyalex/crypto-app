package com.krachkovsky.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.krachkovsky.cryptoapp.R
import com.krachkovsky.cryptoapp.databinding.ItemCoinInfoBinding
import com.krachkovsky.cryptoapp.models.CoinPriceInfo

class CoinInfoAdapter(
    private val context: Context,
    private val onCoinClicked: (CoinPriceInfo) -> Unit
) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    inner class CoinInfoViewHolder(
        private val binding: ItemCoinInfoBinding,
        private val onCoinClicked: (CoinPriceInfo) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coinPriceInfo: CoinPriceInfo) {
            with(binding) {
                with(coinPriceInfo) {
                    ivIcon.load(getFullImageURL()) {
                        scale(Scale.FILL)
                        size(ViewSizeResolver(root))
                    }
                    val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                    val lastUpdateTemplate =
                        context.resources.getString(R.string.last_update_template)
                    tvLastUpdate.text = String.format(lastUpdateTemplate, getFormattedTime())
                    tvMarket.text = Market
                    tvLastMarket.text = lastMarket
                    tvPrice.text = price
                    tvSymbols.text = String.format(symbolsTemplate, fromSymbol, toSymbol)

                    root.setOnClickListener {
                        onCoinClicked(coinPriceInfo)
                    }
                }
            }
        }
    }

    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CoinInfoViewHolder(
            binding = ItemCoinInfoBinding.inflate(layoutInflater, parent, false),
            onCoinClicked = onCoinClicked
        )
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        holder.bind(coin)
    }

    override fun getItemCount() = coinInfoList.size

}