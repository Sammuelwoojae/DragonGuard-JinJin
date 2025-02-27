package com.dragonguard.android.model.compare

//Repository의 멤버의 상세정보를 담기 위한 모델
data class RepoMembersResult (
    val additions: Int,
    val commits: Int,
    val deletions: Int,
    val githubId: String
)