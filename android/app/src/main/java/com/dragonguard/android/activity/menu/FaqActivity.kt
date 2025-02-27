package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.ActivityFaqBinding
import com.dragonguard.android.model.menu.FaqModel
import com.dragonguard.android.recycleradapter.FaqAdapter
import com.dragonguard.android.recycleradapter.HorizontalItemDecorator
import com.dragonguard.android.recycleradapter.VerticalItemDecorator

/*
 자주 묻는 질문들과 답을 보여주는 activity
 */
class FaqActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaqBinding
    private val faqList = arrayListOf(
        FaqModel("로딩이 너무 느려요!", "서버에서 열심히 계산중이예요~~"),
        FaqModel("학교별 랭킹이 보이지 않아요!", "조직 인증을 통해 소속 학교를 인증해야 해요"),
        FaqModel("조직 인증은 어디서 하나요?", "설정 → 학교 인증에서 가능합니다!"),
        FaqModel("정보가 업데이트 되지 않았어요!", "일정 주기마다 업데이트 중이니 조금만 기다려주세요!!"),
        FaqModel("저희 대학교가 대학교 리스트에 없어요!", "메일 주시면 추가해 드리겠습니다. 죄송합니다!"),
        FaqModel("랭킹/티어를 올리고 싶어요!", "commit, issue, pr, code review등등 github활동을 많이많이 해보아요~~"),
        FaqModel("토큰은 어디에 쓰나요?", "현재 토큰은 다른 사람의 포인트를 탈취함을 방지하기 위해 쓰이고 있어요! 금전적인 가치를 지니고 있지는 않답니다."),
        FaqModel("후원하고 싶어요!", "감사하지만 마음만 받을게요!")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.title = "  자주 묻는 질문들!!"
        binding.faqLists.adapter = FaqAdapter(faqList)
        binding.faqLists.addItemDecoration(VerticalItemDecorator(20))
        binding.faqLists.addItemDecoration(HorizontalItemDecorator(10))
        binding.faqLists.layoutManager = LinearLayoutManager(this)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
            R.id.home_menu->{
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}