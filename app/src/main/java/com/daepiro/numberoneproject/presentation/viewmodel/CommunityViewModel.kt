package com.daepiro.numberoneproject.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daepiro.numberoneproject.data.model.CommentWritingResponse
import com.daepiro.numberoneproject.data.model.CommunityDisasterDetailResponse
import com.daepiro.numberoneproject.data.model.CommunityHomeDisasterResponse
import com.daepiro.numberoneproject.data.model.CommunityRereplyRequestBody
import com.daepiro.numberoneproject.data.model.CommunityTownDetailData
import com.daepiro.numberoneproject.data.model.CommunityTownListModel
import com.daepiro.numberoneproject.data.model.CommunityTownReplyRequestBody
import com.daepiro.numberoneproject.data.model.CommunityTownReplyResponse
import com.daepiro.numberoneproject.data.model.ConversationRequestBody
import com.daepiro.numberoneproject.data.model.GetRegionResponse
import com.daepiro.numberoneproject.data.network.onFailure
import com.daepiro.numberoneproject.data.network.onSuccess
import com.daepiro.numberoneproject.domain.usecase.DeleteCommunityReplyUseCase
import com.daepiro.numberoneproject.domain.usecase.DeleteCommunityTownCommentUseCase
import com.daepiro.numberoneproject.domain.usecase.GetCommunityHomeDetailUseCase
import com.daepiro.numberoneproject.domain.usecase.GetCommunityTownDetailUseCase
import com.daepiro.numberoneproject.domain.usecase.GetCommunityTownListUseCase
import com.daepiro.numberoneproject.domain.usecase.GetDisasterHomeUseCase
import com.daepiro.numberoneproject.domain.usecase.GetTownListUseCase
import com.daepiro.numberoneproject.domain.usecase.GetTownReplyUseCase
import com.daepiro.numberoneproject.domain.usecase.PostDisasterConversationUseCase
import com.daepiro.numberoneproject.domain.usecase.SetCommunityTownReplyWritingUseCase
import com.daepiro.numberoneproject.domain.usecase.SetCommunityTownRereplyWritingUseCase
import com.daepiro.numberoneproject.domain.usecase.SetCommunityWritingUseCase

import com.daepiro.numberoneproject.presentation.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val getTownListUseCase:GetTownListUseCase,
    private val getCommunityTownListUseCase: GetCommunityTownListUseCase,
    private val getCommunityTownDetailUseCase: GetCommunityTownDetailUseCase,
    private val setCommunityWritingUseCase: SetCommunityWritingUseCase,
    private val getTownReplyUseCase: GetTownReplyUseCase,
    private val setCommunityTownReplyWritingUseCase: SetCommunityTownReplyWritingUseCase,
    private val setCommunityTownRereplyWritingUseCase: SetCommunityTownRereplyWritingUseCase,
    private val deleteCommunityTownCommentUseCase: DeleteCommunityTownCommentUseCase,
    private val deleteCommunityReplyUseCase: DeleteCommunityReplyUseCase,
    private val getDisasterHomeUseCase: GetDisasterHomeUseCase,
    private val getCommunityHomeDetailUseCase: GetCommunityHomeDetailUseCase,
    private val postDisasterConversationUseCase: PostDisasterConversationUseCase

) : ViewModel() {

    private val _townCommentList = MutableStateFlow(CommunityTownListModel())
    val townCommentList=_townCommentList.asStateFlow()

    private val _townDetail = MutableStateFlow(CommunityTownDetailData())
    val townDetail=_townDetail.asStateFlow()

    val _isVisible = MutableLiveData<Boolean>()
    val isVisible:LiveData<Boolean> = _isVisible

    private val _writingResult = MutableStateFlow(CommentWritingResponse())
    val writingResult = _writingResult.asStateFlow()

    private val _replyResult = MutableStateFlow(CommunityTownReplyResponse())
    val replyResult = _replyResult.asStateFlow()

    val _tagData = MutableLiveData<String>()
    val tagData:LiveData<String> = _tagData

    private val _replycontent= MutableStateFlow("")
    val replycontent:StateFlow<String> = _replycontent.asStateFlow()

    private val _additionalState = MutableStateFlow("")
    val additionalState:StateFlow<String> = _additionalState.asStateFlow()

    private val _disasterHome = MutableStateFlow(CommunityHomeDisasterResponse())
    val disasterHome = _disasterHome.asStateFlow()

    fun updateAdditionalType(input:String){
        _additionalState.value= input
    }

    var id:Int=0

    fun updateContent(input:String){
        _replycontent.value = input
    }
    private val _townList = MutableStateFlow(GetRegionResponse())
    val townList = _townList.asStateFlow()

    val _selectRegion = MutableStateFlow("")
    val selectRegion:StateFlow<String> = _selectRegion.asStateFlow()
    fun getTownList(){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getTownListUseCase(token)
                .onSuccess {
                    _townList.value = it
                    Log.d("getTownList", "$it")
                }
                .onFailure {
                    Log.e("getTownList", "$it")
                }
        }
    }



    fun getTownCommentList(size:Int,tag:String?,lastArticleId:Int?,longtitude: Double?, latitude: Double?,regionLv2:String){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getCommunityTownListUseCase(token,size,tag,lastArticleId,longtitude,latitude,regionLv2)
                .onSuccess {datalist->
                    _townCommentList.value = datalist
                }
                .onFailure {it->
                    //Log.e("CommunityForTownViewModel","$it")
                    if(it is HttpException){
                        Log.e("CommunityForTownViewModel1","$it")
                    }
                    else{
                        Log.e("CommunityForTownViewModel2","${it}")
                    }
                }
        }
    }
    fun getTownDetail(articleId: Int){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getCommunityTownDetailUseCase(token,articleId)
                .onSuccess {deatilData->
                    _townDetail.value = deatilData
                }
                .onFailure {
                    Log.e("getTownDetail","$it")
                }
        }
    }

    fun postComment(title:String, content:String, articleTag:String, imageList: List<MultipartBody.Part>, longtitude:Double, latitude:Double, regionAgreementCheck:Boolean){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            setCommunityWritingUseCase.invoke(token,title,content,articleTag,imageList,longtitude,latitude,regionAgreementCheck)
                .onSuccess {
                    _writingResult.value = it
                    Log.d("CommunityViewModel", "성공성공성공")
                    Log.d("CommunityViewModel", "$it")
                }
                .onFailure {
                    Log.d("CommunityViewModel1", "$it")
                }

        }
    }

    //댓글 조회
    fun setReply(articleId:Int){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getTownReplyUseCase.invoke(token,articleId)
                .onSuccess {response->
                    _replyResult.value = response
                    Log.d("getReply", "$response")
                }
                .onFailure {
                    Log.e("getReply","$it")
                }
        }
    }

    //댓글 작성
    fun writeReply(articleId: Int, body: CommunityTownReplyRequestBody){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            setCommunityTownReplyWritingUseCase.invoke(token,articleId,body)
                .onSuccess {
                    setReply(articleId)
                }
                .onFailure {
                    Log.e("writeReply","$it")
                }
        }
    }

    //대댓글 작성
    fun writeRereply(articleid: Int,commentid:Int,body: CommunityRereplyRequestBody){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            setCommunityTownRereplyWritingUseCase.invoke(token,articleid,commentid,body)
                .onSuccess {
                    setReply(articleid)
                    Log.e("writeRereply","$it")
                }
                .onFailure {
                    Log.e("writeRereply","$it")
                }
        }
    }
    //게시글 삭제
    fun deleteComment(articleId: Int){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            deleteCommunityTownCommentUseCase.invoke(token,articleId)
                .onSuccess{
                    //게시글 삭제 주석처리 추후 포함시켜야함
                    //getTownCommentList(10,null,null,null,null,"신길동")
                }
        }
    }
    //동네생활 댓글 삭제
    fun deleteReply(commentid: Int){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            deleteCommunityReplyUseCase.invoke(token,commentid)
                .onSuccess {
                    townDetail.value?.articleId?.let { it1 -> setReply(it1) }
                }
        }
    }

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> = _isLoading

    //재난상황 api
    fun getDisasterHome(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getDisasterHomeUseCase.invoke(token)
                .onSuccess { response->
                    _disasterHome.value = response
                    _isLoading.value = false
                    Log.d("getDisasterHome", "${response}")
                }
                .onFailure {
                    _isLoading.value = true
                    Log.e("getDisasterHome" , "${isLoading.value}")
                }
        }
    }

    private val _disasterHomeDetail = MutableStateFlow(CommunityDisasterDetailResponse())
    val disasterHomeDetail = _disasterHomeDetail.asStateFlow()

    var disasterId:Int=0

    //재난상황 댓글 모두
    fun getDisasterDetail(sort:String,disasterId:Int){
        viewModelScope.launch {
            _isLoading.value = true
            val token = "Bearer ${tokenManager.accessToken.first()}"
            getCommunityHomeDetailUseCase.invoke(token,sort,disasterId)
                .onSuccess { response->
                    _disasterHomeDetail.value = response
                    _isLoading.value = false
                    Log.d("getDisasterDetail", "${response}")
                }
                .onFailure {
                    _isLoading.value = true
                    Log.d("getDisasterDetail", "${it}")
                }
        }
    }

    fun postDisasterConversation(body: ConversationRequestBody){
        viewModelScope.launch {
            val token = "Bearer ${tokenManager.accessToken.first()}"
            postDisasterConversationUseCase(token,body)
                .onSuccess {
                    getDisasterDetail("time", body.disasterId)
                }
        }
    }



    val tagText: StateFlow<String> = townDetail
        .map { detail -> tagTextForDetail(detail.articleTag) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )
    @RequiresApi(Build.VERSION_CODES.O)
    val detailTime:StateFlow<String> = townDetail
        .map { detail -> getTimeDifference(detail.createdAt) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )


    private fun tagTextForDetail(articleTag:String):String{
        return when(articleTag){
            "SAFETY" -> "안전"
            "LIFE" -> "일상"
            "TRAFFIC" -> "교통"
            else->"기타"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeDifference(createdTime: String): String {
        if (createdTime.isBlank()) {
            return "기본값 또는 오류 메시지"//createdAt":"2023-11-22 08:21
        }

        val formatters = listOf(//2023-11-22 08:21
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
        var parsedDateTime : LocalDateTime? = null
        for(formatter in formatters){
            try {
                parsedDateTime = LocalDateTime.parse(createdTime, formatter)
                val currentDateTime = LocalDateTime.now()
                val duration = Duration.between(parsedDateTime, currentDateTime)
                return when {
                    duration.toHours() < 1 -> "${duration.toMinutes()}분 전"
                    duration.toDays() < 1 -> String.format("%02d:%02d",parsedDateTime.hour,parsedDateTime.minute)
                    else -> "${parsedDateTime.monthValue}/${parsedDateTime.dayOfMonth}"
                }
            } catch (e: DateTimeParseException) {
                // 현재 형식으로 파싱 실패; 다음 형식으로 시도
                Log.e("getTimeDifference", "$e")

            }
        }
        return "파싱 오류"
    }
    init{
        _isLoading.value = true
        getTownList()
    }
}

