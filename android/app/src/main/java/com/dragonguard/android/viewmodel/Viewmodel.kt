package com.dragonguard.android.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dragonguard.android.connect.ApiRepository
import com.dragonguard.android.model.*
import com.dragonguard.android.model.compare.CompareRepoMembersResponseModel
import com.dragonguard.android.model.compare.CompareRepoRequestModel
import com.dragonguard.android.model.compare.CompareRepoResponseModel
import com.dragonguard.android.model.contributors.RepoContributorsItem
import com.dragonguard.android.model.detail.UserDetailModel
import com.dragonguard.android.model.klip.*
import com.dragonguard.android.model.org.*
import com.dragonguard.android.model.rankings.OrgInternalRankingModel
import com.dragonguard.android.model.rankings.OrganizationRankingModel
import com.dragonguard.android.model.rankings.TotalUsersRankingModelItem
import com.dragonguard.android.model.search.RepoSearchResultModel
import com.dragonguard.android.model.token.RefreshTokenModel
import kotlinx.coroutines.*

/*
 mvvm 구현을 위한 viewmodel
 */
class Viewmodel: ViewModel() {
    private val repository = ApiRepository()
    var onSearchClickListener = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()
    var onOptionListener = MutableLiveData<String>()
    var onSearchListener = MutableLiveData<String>()
    var onAuthEmailListener = MutableLiveData<String>()
    var onApproveOrgListener = MutableLiveData<Boolean>()

    val customTimerDuration: MutableLiveData<Long> = MutableLiveData(MIllIS_IN_FUTURE)
    private var oldTimeMills: Long = 0

    companion object {
        const val MIllIS_IN_FUTURE = 300000L
        const val TICK_INTERVAL = 1000L
    }

    val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO) {
            oldTimeMills = System.currentTimeMillis()
            while (customTimerDuration.value!! > 0L) {
                val delayMills = System.currentTimeMillis() - oldTimeMills
                if (delayMills == TICK_INTERVAL) {
                    customTimerDuration.postValue(customTimerDuration.value!! - delayMills)
                    oldTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

//
    fun getUserInfo(token: String): UserInfoModel{
        Log.d("token", "Bearer : $token")
        return repository.getUserInfo(token)
    }

    fun getSearchRepoResult(name: String, count: Int, type: String, token: String): ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNames(name, count, type, token)
    }

    fun getRepositoryNamesWithFilters(name:String, count:Int, filters: String, type: String, token: String):ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNamesWithFilters(name, count, filters, type, token)
    }


    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchIcon() {
        onSearchClickListener.value = true
    }

    fun getRepoContributors(repoName: String, token: String): ArrayList<RepoContributorsItem> {
        return repository.getRepoContributors(repoName, token)
    }

    fun clickSearchOption(){
        if(onOptionListener.value == "up"){
            onOptionListener.value = "down"
        }else{
            onOptionListener.value = "up"
        }
    }

    fun getTotalUserRanking(page: Int, size: Int, token: String): ArrayList<TotalUsersRankingModelItem> {
        return repository.getTotalUsersRankings(page, size, token)
    }

    fun postWalletAuth(body: WalletAuthRequestModel): WalletAuthResponseModel {
        return repository.postWalletAuth(body)
    }

    fun getWalletAuthResult(key: String): WalletAuthResultModel {
        return repository.getAuthResult(key)
    }

    fun getTokenHistory(id: Long, token: String): ArrayList<TokenHistoryModelItem> {
        return repository.getTokenHistory(id, token)
    }

    fun postWalletAddress(walletAddress: String, token: String): Boolean {
        val body = WalletAddressModel(walletAddress)
        return repository.postWalletAddress(body, token)
    }

    fun postCompareRepoMembersRequest(firstRepo: String, secondRepo: String, token: String): CompareRepoMembersResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoMembersRequest(body, token)
    }

    fun postCompareRepoRequest(firstRepo: String, secondRepo: String, token: String): CompareRepoResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoRequest(body, token)
    }

    fun getNewToken(access: String, refresh: String): RefreshTokenModel {
        return repository.getNewAccessToken(access, refresh)
    }

    fun postCommits(token: String) {
        repository.postCommits(token)
    }

    fun getOrgNames(name: String, token: String,type: String, page: Int): OrganizationNamesModel {
        return repository.getOrgNames(name, token, page, type)
    }

    fun registerOrg(name: String, orgType: String, emailEndPoint: String, token: String): RegistOrgResultModel {
        return repository.postRegistOrg(RegistOrgModel(emailEndpoint = emailEndPoint, organizationType = orgType, name = name), token)
    }

    fun addOrgMember(orgId: Long, email: String, token: String): Long{
        return repository.addOrgMember(AddOrgMemberModel(email, orgId), token)
    }

    fun sendEmailAuth(token: String): Long {
        return repository.sendEmailAuth(token)
    }

    fun emailAuthResult(id: Long, code: String, token: String): Boolean {
        return repository.emailAuthResult(id, code, token)
    }

    fun deleteLateEmailCode(id: Long, token: String): Boolean {
        return repository.deleteEmailCode(id, token)
    }

    fun searchOrgId(name: String, token: String): Long {
        Log.d("name", "조직이름: $name")
        Log.d("토큰", "토큰 : $token")
        return repository.searchOrgId(name, token)
    }

    fun orgInterRankings(id: Long, page: Int, token: String): OrgInternalRankingModel {
        return repository.orgInternalRankings(id, page, token)
    }

    fun typeOrgRankings(type: String, page: Int, token: String): OrganizationRankingModel {
        return repository.typeOrgRanking(type, page, token)
    }

    fun totalOrgRankings(page: Int, token: String): OrganizationRankingModel {
        return repository.allOrgRanking(page, token)
    }

    fun userDetails(id: String, token: String): UserDetailModel {
        return repository.userDetail(id, token)
    }

    fun checkAdmin(token: String): Boolean {
        return repository.checkAdmin(token)
    }

    fun approveOrgRequest(id:Long, decide: String, token: String): ApproveRequestOrgModel {
        val body = OrgApprovalModel(decide, id)
        return repository.approveOrgRequest(body, token)
    }

    fun statusOrgList(status: String, page: Int, token: String): ApproveRequestOrgModel {
        return repository.statusOrgList(status, page, token)
    }
}