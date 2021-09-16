package com.example.geideaproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.geideaproject.R
import com.example.geideaproject.data.api.User
import com.example.geideaproject.databinding.ItemListContentBinding

class NewsListAdapter constructor(val glide:RequestManager): RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    var clickListener:((itemView:View , user: User)-> Unit)? = null
    var users:MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvSource?.text = "user ID #${user.id}"
        holder.tvTitle?.text = user.first_name + " "+user.last_name

        glide.load(user.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_news_placeholder)
            .into(holder.ivPic);

        with(holder.itemView) {
            tag = user
            setOnClickListener {
                clickListener?.invoke(it,user)
            }
        }
    }

    override fun getItemCount() = users.size
    fun addNews(pagingList: MutableList<User>) {
        users.addAll(pagingList)
    }

    fun appendNews(pagingList: MutableList<User>) {
        val oldSize = users.size
        users.addAll(pagingList)
        notifyItemRangeInserted(oldSize,pagingList.size)
    }

    fun clearNews(){
        users.clear()
        notifyDataSetChanged()
    }

    fun searchNews(query:String){
       users = users.filter { it.email.equals(query)}.toMutableList()
    }

    fun addUsers(userList: MutableList<User>) {
        this.users.clear()
        this.users.addAll(userList)
    }

    inner class ViewHolder(binding: ItemListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle: AppCompatTextView = binding.tvTitle
        val tvSource: AppCompatTextView = binding.tvSource
        val ivPic: AppCompatImageView = binding.ivPic
    }

}