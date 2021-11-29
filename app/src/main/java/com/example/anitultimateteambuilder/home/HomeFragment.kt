package com.example.anitultimateteambuilder.home

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anitultimateteambuilder.R
import com.example.anitultimateteambuilder.databinding.HomeFragmentBinding
import com.example.anitultimateteambuilder.network.WebAccess
import com.example.anitultimateteambuilder.players.PlayersListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Cache
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: HomeViewModel
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val binding = HomeFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        binding.listViewNews.layoutManager = LinearLayoutManager(context)
        binding.listViewNews.adapter = NewsAdapter(context,emptyList(),R.layout.news_item_cv) {}


        viewModel.news.observe(viewLifecycleOwner){
//            (binding.listViewNews.adapter as NewsAdapter).listOfNews = it
//            binding.listViewNews.adapter?.notifyDataSetChanged()
            val recyclerViewState = binding.listViewNews.layoutManager?.onSaveInstanceState()
            val oldListOfNews = (binding.listViewNews.adapter as NewsAdapter).listOfNews
            val diffResult :DiffUtil.DiffResult = DiffUtil.calculateDiff(NewsAdapter.MyDiffCallback(it,oldListOfNews))
            (binding.listViewNews.adapter as NewsAdapter).listOfNews = it
            diffResult.dispatchUpdatesTo(binding.listViewNews.adapter as NewsAdapter)
//            binding.listViewNews.layoutManager?.onRestoreInstanceState(recyclerViewState)

        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            if (it) {
                binding.indeterminateBar.visibility = View.VISIBLE
            } else
            {
                binding.indeterminateBar.visibility = View.INVISIBLE
            }
        }

        binding.listViewNews.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.listViewNews.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
//                    Toast.makeText(requireContext(),"The end",Toast.LENGTH_SHORT).show()
                    if (viewModel.isLoading.value != true)
                        loadPartsAndUpdateList()
                }
            }
        })

        onClickRequestPermission()
//        loadPartsAndUpdateList()

        return binding.root
    }

    private fun loadPartsAndUpdateList() {
        // Launch Kotlin Coroutine on Android's main thread
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.setLoadingState(true)
            try {

//                WebAccess.usingCache = viewModel.news.value?.isNotEmpty() == true
                // Execute web request through coroutine call adapter & retrofit
                val webResponse = WebAccess.partsApi.getNewsAsync("football","2021-11-16",
                    resources.getString(R.string.news_api_key),viewModel.cPage).await()

                if (webResponse.isSuccessful) {
                    if ( webResponse.body() != null )
                    {
                        viewModel.updateNews(webResponse.body()!!.articles)
                        if (webResponse.raw().networkResponse() != null) {
                            viewModel.cPage = viewModel.cPage + 1
                        }
                    }
                } else {
                    // Print error information to the console
                    Log.d(tag, "Error ${webResponse.code()}")
                }
            } catch (e: IOException) {
                // Error with network request
                Log.e(tag, "Exception " + e.printStackTrace())
            }
            viewModel.setLoadingState(false)
        }

    }

    fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED -> {}

            shouldShowRequestPermissionRationale(
                Manifest.permission.INTERNET
            ) -> {requestPermissionLauncher.launch(Manifest.permission.INTERNET)}

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.INTERNET
                )
            }


        }
    }

}