package com.daepiro.numberoneproject.data.model

data class CommunityHomeSituationModel(
    val conversationCnt: Int=0,
    val conversations: Array<CommunityHomeConversationModel> = arrayOf(),
    val disasterId: Int = 0,
    val disasterType: String = "",
    val info: String = "",
    val msg: String = "",
    val title: String = ""
)