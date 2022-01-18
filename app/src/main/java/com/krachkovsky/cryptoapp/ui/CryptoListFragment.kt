package com.krachkovsky.cryptoapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krachkovsky.cryptoapp.adapters.CoinInfoAdapter
import com.krachkovsky.cryptoapp.databinding.FragmentCoinListBinding
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PAGE_SIZE
import com.krachkovsky.cryptoapp.view_models.CryptoListFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CryptoListFragment : Fragment() {

    private val viewModel: CryptoListFragmentViewModel by viewModel()

    private var _binding: FragmentCoinListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCoinListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CoinInfoAdapter(requireContext()) { coin ->
            findNavController().navigate(
                CryptoListFragmentDirections.actionCryptoListFragmentToCoinDetailsFragment(coin.fromSymbol)
            )
        }

        with(binding) {
            rvMain.adapter = adapter
            rvMain.layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            rvMain.addOnScrollListener(scrollListener)
            viewModel.priceList.observe(viewLifecycleOwner, {
                adapter.coinInfoList = it
            })
        }

    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.addPage()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startRequest()
        viewModel.loadCoinData()
        Log.d("AAA", "onStartFragment")
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopRequest()
        Log.d("AAA", "onStopFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}