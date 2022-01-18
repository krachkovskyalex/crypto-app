package com.krachkovsky.cryptoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.krachkovsky.cryptoapp.R
import com.krachkovsky.cryptoapp.databinding.FragmentCoinDetailBinding
import com.krachkovsky.cryptoapp.view_models.CoinDetailsFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CoinDetailsFragment : Fragment() {

    private val viewModel: CoinDetailsFragmentViewModel by viewModel()

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val args: CoinDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCoinDetailBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCoinDetailInfo(args.fromSym).observe(viewLifecycleOwner, {
            with(binding) {
                with(requireNotNull(context).resources) {
                    with(it) {
                        val price = getString(R.string.price)
                        val minPerDay = getString(R.string.min_per_day)
                        val maxPerDay = getString(R.string.max_per_day)
                        val lastDeal = getString(R.string.last_market)
                        val lastUpdate = getString(R.string.last_update)
                        ivDetailLogo.load(getFullImageURL()) {
                            scale(Scale.FILL)
                            size(ViewSizeResolver(root))
                        }
                        tvDetailFromSym.text = fromSymbol
                        tvDetailToSym.text = toSymbol
                        tvPrice.text = String.format(price, it.price)
                        tvMinPerDay.text = String.format(minPerDay, lowDay)
                        tvMaxPerDay.text = String.format(maxPerDay, highDay)
                        tvLastMarket.text = String.format(lastDeal, lastMarket)
                        tvLastUpdate.text = String.format(lastUpdate, getFormattedTime())

                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.startRequest()
        viewModel.loadCoinData(args.fromSym)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}