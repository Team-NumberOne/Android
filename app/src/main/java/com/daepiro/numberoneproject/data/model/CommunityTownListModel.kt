package com.daepiro.numberoneproject.data.model

data class CommunityTownListModel(
    val content: List<Content> = listOf(),
    val empty: Boolean = true,
    val first: Boolean=true,
    val last: Boolean=true,
    val number: Int=0,
    val numberOfElements: Int=0,
    val pageable: String="" ,
    val size: Int=0,
    val sort: Sort = Sort()
)
data class Content(
    val address: String="",
    val articleLikeCount: Int=0,
    val articleStatus: String="",
    val commentCount: Int=0,
    val content: String="",
    val createdAt: String="",
    val id: Int=0,
    val ownerId: Int=0,
    val ownerNickName: String="",
    val tag: String="",
    val thumbNailImageId: Int=0,
    val thumbNailImageUrl: String="",
    val title: String="",
    val isLiked:Boolean = true
)

data class Sort(
    val empty: Boolean=true,
    val sorted: Boolean=true,
    val unsorted: Boolean=true
)

data class PageableCommunity(
    val offset: Int = 0,
    val sort: Sort2 = Sort2(),
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val paged: Boolean = true,
    val unpaged: Boolean = true,
)

data class Sort2(
    val empty: Boolean = true,
    val sorted: Boolean = true,
    val unsorted: Boolean = true,
)