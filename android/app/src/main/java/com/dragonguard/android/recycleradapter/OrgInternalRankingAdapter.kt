package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.UserDetailActivity
import com.dragonguard.android.databinding.TotalUsersRankingListBinding
import com.dragonguard.android.model.rankings.OrgInternalRankingsModel

class OrgInternalRankingAdapter (private val datas : ArrayList<OrgInternalRankingsModel>,
                                 private val context: Context,
                                 private val token: String) : RecyclerView.Adapter<OrgInternalRankingAdapter.ViewHolder>() {
    private lateinit var binding: TotalUsersRankingListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TotalUsersRankingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data1: OrgInternalRankingsModel) {
            if(binding.totalUsersRanking.text.isNullOrEmpty() && binding.rankerId.text.isNullOrEmpty() && binding.rankerContribution.text.isNullOrEmpty()) {
                binding.totalUsersRanking.text = data1.ranking.toString()
                binding.rankerId.text = data1.githubId
//                Toast.makeText(context, "${data.tokens}", Toast.LENGTH_SHORT).show()
                if(data1.tokens == null) {
                    binding.rankerContribution.text = "NONE"
                } else {
                    binding.rankerContribution.text = data1.tokens.toString()
                }

                binding.rankerId.setOnClickListener {
                    Intent(context, UserDetailActivity::class.java).apply{
                        putExtra("githubId", data1.githubId)
                        putExtra("token", token)
                    }.run{context.startActivity(this)}
                }
            }
        }
//        fun bind(data1: TotalUsersRankingsModel, data2: TotalUsersRankingsModel) {
//            if(ranking.text.isNullOrEmpty() && githubId.text.isNullOrEmpty() && contribution.text.isNullOrEmpty()) {
//                if(data1.commits == data2.commits) {
//                    ranking.text = data2.ranking.toString()
//                } else {
//                    ranking.text = data1.ranking.toString()
//                }
//                githubId.text = data1.githubId
//                contribution.text = data1.commits.toString()
//            }
//        }
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