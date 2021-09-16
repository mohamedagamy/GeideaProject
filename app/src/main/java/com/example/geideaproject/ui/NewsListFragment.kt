package com.example.geideaproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.geideaproject.R
import com.example.geideaproject.data.api.User
//import com.example.geideaproject.data.interceptor.showToast
import com.example.geideaproject.data.model.Status
import com.example.geideaproject.data.repo.Constant
import com.example.geideaproject.data.room.AppDatabase
import com.example.geideaproject.databinding.FragmentNewsListBinding
import com.example.tempo.paging.EndlessRecyclerViewScrollListener
import com.example.geideaproject.ui.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.sql.Time

@AndroidEntryPoint
class NewsListFragment : Fragment() {
    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding
    lateinit var newsAdapter: NewsListAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    var searchKey:String = ""
    var pageNum:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    private fun initViews() {
        setUpAdapter()
        initSwipeRefresh()
        getNewsList(20)
    }

    @SuppressLint("CheckResult")
    private fun getNewsList(pageNum: Int) {
        binding?.swipeToRefresh?.isRefreshing = true
        (activity as MainActivity).appViewModel.getAllUsers(pageNum)
            .observe(viewLifecycleOwner,{
                binding?.swipeToRefresh?.isRefreshing = false
                Timber.d("status: ${it.status}")
                when(it.status){
                    Status.SUCCESS -> {
                        Timber.d("SUCCESS")
                        Timber.d("data: ${it.data}")
                        saveLocally(it.data?.data)
                    }
                    Status.ERROR -> {
                        Timber.d("ERROR")
                        //context?.showToast("server error")
                    }
                    Status.LOADING -> {
                        Timber.d("LOADING")
                        binding?.swipeToRefresh?.isRefreshing = true
                    }
                }

        })
    }

    private fun saveLocally(users: List<User>?) {
        try {
            users?.let {
                AppDatabase.getAppDataBase(requireContext())?.userDao()?.insertAll(it?.get(0))
            }
        } catch (e: Exception) {
            Timber.e(""+e.printStackTrace())
        }
    }



    private fun showEmptyHolder(isShown: Boolean) {
        if(isShown){
            binding?.rvNewsList?.visibility = View.GONE
            binding?.ivPlaceholder?.visibility = View.VISIBLE
        }else{
            binding?.rvNewsList?.visibility = View.VISIBLE
            binding?.ivPlaceholder?.visibility = View.GONE
        }
    }

    private fun initSwipeRefresh() {
        binding?.swipeToRefresh?.setOnRefreshListener {
            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)
                binding?.swipeToRefresh?.isRefreshing = false
            }, 200) // Delay in millis
        }
    }

    private fun setUpAdapter() {
        val rvNewsList: RecyclerView? = binding?.rvNewsList
        layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsListAdapter((activity as MainActivity).glide)
        rvNewsList?.layoutManager =  layoutManager
        rvNewsList?.adapter = newsAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMoreData()
                Timber.d("pageNumber: ${page} itemCount: ${totalItemsCount} adapter size: ${newsAdapter.itemCount}")
            }

        }
        //rvNewsList?.addOnScrollListener(scrollListener)

        val itemDetailFragmentContainer: View? = view?.findViewById(R.id.item_detail_nav_container)
        //handle item click
        newsAdapter.clickListener = { itemView, item ->
            val bundle = bundleOf(Constant.ARG_ITEM_ID to item.id.toString())
            if(itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController().navigate(R.id.fragment_item_detail, bundle)
            }else{
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        (activity as MainActivity).appViewModel.newsList.observe(viewLifecycleOwner,{
            newsAdapter.addUsers(it.data.toMutableList())
            showEmptyHolder(newsAdapter.itemCount == 0)
        })
    }

    private fun loadMoreData() {
        pageNum++
        if(pageNum < 6)
            getNewsList(pageNum)
    }

    override fun onDestroyView() {
        Timber.d("adapter destroy size ${newsAdapter.users.size}")
        super.onDestroyView()
        _binding = null
    }
}