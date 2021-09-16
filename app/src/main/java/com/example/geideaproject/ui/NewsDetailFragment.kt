package com.example.geideaproject.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.geideaproject.data.api.UserDetails
import com.example.geideaproject.data.model.Status
import com.example.geideaproject.databinding.FragmentNewsDetailBinding
import com.example.geideaproject.ui.countdown.BroadcastService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    //private var userDetails: UserDetails? = null
    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding
    lateinit var broadcastReceiver: BroadcastReceiver
    lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userID = (arguments?.get("item_id") as String).toInt()
        getUserByID(userID)
        //(activity as MainActivity).startCountDown()
        startCounter()
    }

    private fun startCounter() {
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i("", "Countdown seconds remaining: " + millisUntilFinished / 1000)

            }

            override fun onFinish() {
                (activity as MainActivity).goToMainFragment()
            }
        }

        countDownTimer.start()
    }

    override fun onStart() {
        super.onStart()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Timber.d("service is done")
            }
        }

        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.COUNTDOWN_BR))
        }

        //(activity as MainActivity).registerReceiver(broadcastReceiver, IntentFilter(BroadcastService.COUNTDOWN_BR));
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            countDownTimer.cancel()
            LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver)
        }
    }

    @SuppressLint("CheckResult")
    private fun getUserByID(userID: Int) {
        //binding?.swipeToRefresh?.isRefreshing = true
        (activity as MainActivity).appViewModel.getUserByID(userID)
            .observe(viewLifecycleOwner,{
                //binding?.swipeToRefresh?.isRefreshing = false
                Timber.d("status: ${it.status}")
                //userDetails = it.data
                when(it.status){
                    Status.SUCCESS -> {
                        Timber.d("SUCCESS")
                        Timber.d("data: ${it.data}")
                        showUserDetails(it.data)
                    }
                    Status.ERROR -> {
                        Timber.d("ERROR")
                        //context?.showToast("server error")
                    }
                    Status.LOADING -> {
                        Timber.d("LOADING")
                        //binding?.swipeToRefresh?.isRefreshing = true
                    }
                }

            })
    }

    private fun showUserDetails(userDetails: UserDetails?) {
        val tvDesc = binding?.itemDesc
        val tvAuthor = binding?.itemAuthor
        val tvContent = binding?.itemContent
        val tvDate = binding?.itemDate
        val tvSource = binding?.itemSource
        val tvTitle = binding?.itemTitle
        // Show the placeholder content as text in a TextView.
        userDetails?.data?.let {
            tvDesc?.text = it.email
            tvAuthor?.text = it.first_name
            tvContent?.text = it.last_name
            tvDate?.text = it.id.toString()
            tvSource?.text = "user ID #${it.id}"
            tvTitle?.text = it.first_name + " "+it.last_name
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        val rootView = binding?.root

        binding?.toolbarLayout?.title = "USER DETAILS"
        binding?.fab?.setOnClickListener {
            //context?.openLink(user?.user?.avatar ?: "")
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}