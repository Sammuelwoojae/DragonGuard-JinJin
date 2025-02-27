package com.dragonguard.android.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentCompareRepoBinding
import com.dragonguard.android.model.compare.CompareRepoMembersResponseModel
import com.dragonguard.android.model.compare.CompareRepoResponseModel
import com.dragonguard.android.recycleradapter.RepoCompareAdapter
import com.dragonguard.android.recycleradapter.RepoCompareChartAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//선택한 두 Repository를 비교하기 위한 fragment
class CompareRepoFragment(repoName1: String, repoName2: String, token: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var repo1 = repoName1
    private var repo2 = repoName2
    private var viewmodel = Viewmodel()
    private lateinit var binding : FragmentCompareRepoBinding
    private var count = 0
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f
    private val compareItems = arrayListOf("forks", "close된\n 이슈 수", "open된\n이슈 수", "스타 수", "구독자 수", "watchers 수", "총 커밋 수",
    "최대 커밋수", "최소 커밋 수", "커밋\n기여자 수", "평균 커밋 수", "총\naddtions", "최대\naddtions", "최소\nadditions", "additions\n기여자 수",
    "평균\naddtions", "총\ndeletions", "최대\ndeletions", "최소\ndeletions", "deletions\n기여자 수", "평균\ndeletions", "사용된\n언어 수", "평균 코드 수")
    private val token = token


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_repo, container, false)
        binding.compareRepoViewmodel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    //activity 구성 이후 화면을 초기화하는 함수
    private fun updateUI() {
        repoContributors()
    }

    /*비교 전에 멤버를 불러오는 함수
    호출 후 이상유무를 확인하는 함수 호출
     */
    fun repoContributors() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repo1, repo2, token)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    /*
    멤버를 불러오는 함수의 결과의 이상 유뮤 확인 함수
    이상없으면 비교하는 함수 호출
     */

    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.firstResult != null) && (result.secondResult != null)) {
            if (result.firstResult.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            } else {
                val name1 = repo1.split("/")
                val name2 = repo2.split("/")
                if(name1.size > 2) {
                    binding.compareRepo1.text = "${name1.last()}"
                } else {
                    binding.compareRepo1.text = name1.last()
                }
                if(name2.size > 2) {
                    binding.compareRepo2.text = "${name2.last()}"
                } else {
                    binding.compareRepo2.text = name2.last()
                }
                count = 0
                repoCompare()
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            }
        }
    }

    /*
    두 Repository를 비교하는 API를 호출하는 함수
    호출 후 이상유무를 확인하는 함수 호출
     */
    private fun repoCompare() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoRequest(repo1, repo2, token)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkRepos(result)
        }
    }

    /*
    비교하는 API의 결과의 이상유뮤 확인 후
    recyclerview 그리는 함수 호출
     */
    private fun checkRepos(result: CompareRepoResponseModel) {
        if(result.firstRepo != null && result.secondRepo != null) {
            try {
                result.firstRepo.gitRepo!!
                result.firstRepo.statistics!!
                result.firstRepo.languagesStats!!
                result.firstRepo.languages!!
                result.secondRepo.gitRepo!!
                result.secondRepo.statistics!!
                result.secondRepo.languagesStats!!
                result.secondRepo.languages!!
                initRecycler(result)

            } catch (e: Exception) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        }
    }

    /*
    두 Repository를 비교하기 위한 표를 그리는 recyclerview
    결과에 문제 없으면 다 그리고 그래프를 그리는 함수 호출
     */
    private fun initRecycler(result: CompareRepoResponseModel) {
        Log.d("initRecycler()", "리사이클러뷰 구현 시작")
        if(result.firstRepo!!.languagesStats == null || result.secondRepo!!.languagesStats == null) {
            Log.d("initRecycler()", "${result.firstRepo.languagesStats} 혹은 ${result.secondRepo!!.languagesStats}이 널입니다.")
            count++
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({repoCompare()}, 5000)
        } else {
            val repoCompareAdapter = RepoCompareAdapter(result.firstRepo!!, result.secondRepo!!, compareItems)
            binding.repoCompareList.adapter = repoCompareAdapter
            binding.repoCompareList.layoutManager = LinearLayoutManager(requireContext())
            repoCompareAdapter.notifyDataSetChanged()
            binding.repoCompareList.visibility = View.VISIBLE
            initGraph(result)
        }
    }

    /*
    두 Repository를 비교하기 위한 그래프를 그리는 함수
    가로로 슬라이딩하며 애니메이션 적용함
     */
    private fun initGraph(result: CompareRepoResponseModel) {
        binding.repoCompareChartViewpager.adapter = RepoCompareChartAdapter(result.firstRepo!!, result.secondRepo!!, requireContext())
        binding.repoCompareChartViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.repoCompareChartViewpager.setPageTransformer(ZoomOutPageTransformer())
        binding.repoCompareChartViewpager.isUserInputEnabled = true

    }

    //viewpager의 슬라이딩 애니메이션을 넣는 클래스
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

}