package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.UserDetailActivity
import com.dragonguard.android.databinding.ContributorsListBinding
import com.dragonguard.android.model.contributors.RepoContributorsItem

/*
 선택한 repo의 contributor들의 정보를 나열하기 위한 recycleradapter
 */
class ContributorsAdapter (private val datas : ArrayList<RepoContributorsItem>, private val context: Context, private val colors: ArrayList<Int>,
                           private val token: String, private val repoName: String) : RecyclerView.Adapter<ContributorsAdapter.ViewHolder>() {
    private lateinit var binding: ContributorsListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ContributorsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: RepoContributorsItem) {
            binding.contributeRanking.text = data1.commits.toString()
            binding.contrubutorId.text = data1.githubId
            val red = (Math.random()*255).toInt()
            val green = (Math.random()*255).toInt()
            val blue = (Math.random()*255).toInt()
            binding.contributorColor.imageTintList = ColorStateList.valueOf(Color.rgb(red,green,blue))
            colors.add(Color.rgb(red,green,blue))
            binding.contributorsLayout.setOnClickListener {
                Intent(context, UserDetailActivity::class.java).apply{
                    putExtra("githubId", data1.githubId)
                    putExtra("token", token)
                }.run{context.startActivity(this)}
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    
}