package com.daepiro.numberoneproject.data.network

import com.daepiro.numberoneproject.data.model.CommentWritingRequestBody
import com.daepiro.numberoneproject.data.model.CommentWritingResponse
import com.daepiro.numberoneproject.data.model.CommunityDisasterDetailResponse
import com.daepiro.numberoneproject.data.model.CommunityHomeDisasterResponse
import com.daepiro.numberoneproject.data.model.CommunityHomeSituationModel
import com.daepiro.numberoneproject.data.model.CommunityRereplyRequestBody
import com.daepiro.numberoneproject.data.model.CommunityTownDeleteCommentResponse
import com.daepiro.numberoneproject.data.model.CommunityTownDetailData
import com.daepiro.numberoneproject.data.model.CommunityTownListModel
import com.daepiro.numberoneproject.data.model.CommunityTownReplyDeleteResponse
import com.daepiro.numberoneproject.data.model.CommunityTownReplyRequestBody
import com.daepiro.numberoneproject.data.model.CommunityTownReplyResponse
import com.daepiro.numberoneproject.data.model.CommunityTownReplyResponseModel
import com.daepiro.numberoneproject.data.model.DisasterRequestBody
import com.daepiro.numberoneproject.data.model.DisasterResponse
import com.daepiro.numberoneproject.data.model.FundingDetailResponse
import com.daepiro.numberoneproject.data.model.FundingListResponse
import com.daepiro.numberoneproject.data.model.LoginTestResponse
import com.daepiro.numberoneproject.data.model.LoginTokenResponse
import com.daepiro.numberoneproject.data.model.ShelterData
import com.daepiro.numberoneproject.data.model.ShelterListResponse
import com.daepiro.numberoneproject.data.model.ShelterRequestBody
import com.daepiro.numberoneproject.data.model.TokenRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 만료된 AccessToken 갱신
    @POST("/token/refresh")
    suspend fun refreshAccessToken(
        @Body body: TokenRequestBody
    ): ApiResult<LoginTokenResponse>

    // 네이버 로그인
    @POST("/token/naver")
    suspend fun userNaverLogin(
        @Body body: TokenRequestBody
    ): ApiResult<LoginTokenResponse>

    //카카오 로그인
    @POST("/token/kakao")
    suspend fun userKakaoLogin(
        @Body body: TokenRequestBody
    ): ApiResult<LoginTokenResponse>

    // 로그인 테스트
    @GET("/api/logintest")
    suspend fun apiLoginTest(
        @Header("Authorization") token: String
    ): ApiResult<LoginTestResponse>

    // 가까운 대피소 목록 조회
    @POST("/api/shelters/list")
    suspend fun getAroundSheltersList(
        @Header("Authorization") token: String,
        @Body body: ShelterRequestBody
    ): ApiResult<ShelterListResponse>

    // 최근 재난문자 조회
    @POST("/api/disaster/latest")
    suspend fun getDisasterMessage(
        @Header("Authorization") token: String,
        @Body body: DisasterRequestBody
    ): ApiResult<DisasterResponse>

    //대피소 전체 데이터 가진 링크 가져오기
    @GET("/api/admin/address-info")
    suspend fun getShelters(
        @Header("Authorization") token:String
    ): ApiResult<List<ShelterData>>


    // 후원 목록 최신순 조회
    @GET("/api/sponsor/latest")
    suspend fun getFundingListByLatest(
        @Header("Authorization") token:String
    ): ApiResult<FundingListResponse>

    // 후원 목록 인기순 조회
    @GET("/api/sponsor/popular")
    suspend fun getFundingListByPopular(
        @Header("Authorization") token:String
    ): ApiResult<FundingListResponse>

    // 후원 상세정보 조회
    @GET("/api/sponsor/{sponsorId}")
    suspend fun getFundingDetail(
        @Header("Authorization") token:String,
        @Path("sponsorId") sponsorId: Int
    ): ApiResult<FundingDetailResponse>

    //커뮤니티 동네생활 게시글 리스트 조회
    @GET("/api/articles")
    suspend fun getTownCommentList(
        @Header("Authorization") token:String,
        @Query("size") size:Int,
        @Query("tag") tag:String,
        @Query("lastArticleId") lastArticleId:Int?,
        //@Query("longtitude") longtitude:Double?,
        //@Query("latitude") latitude:Double?,
        @Query("regionLv2") regionLv2:String
    ):ApiResult<CommunityTownListModel>

    //커뮤니티 동네생활 게시글 상세 조회
    @GET("/api/articles/{articleid}")
    suspend fun getTownCommentDetail(
        @Header("Authorization") token:String,
        @Path("articleid") articleid:Int
    ):ApiResult<CommunityTownDetailData>

    //커뮤니티 동네생활 게시글 작성
    @Multipart
    @POST("/api/articles")
    suspend fun setTownDetail(
        @Header("Authorization") token:String,
        @Part("title") title:RequestBody,
        @Part("content") content:RequestBody,
        @Part("articleTag") articleTag:RequestBody,
        @Part("longitude") longitude:RequestBody?,
        @Part("latitude") latitude:RequestBody?,
        @Part("regionAgreementCheck") regionAgreementCheck:RequestBody,
        @Part imageList: List<MultipartBody.Part>
    ):ApiResult<CommentWritingResponse>

    //커뮤니티 동네생활 댓글 조회
    @GET("/api/comments/{articleid}")
    suspend fun getTownReply(
        @Header("Authorization") token:String,
        @Path("articleid") articleid:Int
    ):ApiResult<CommunityTownReplyResponse>

    //커뮤니티 동네생활 댓글 작성
    @POST("/api/articles/comments/{articleid}")
    suspend fun setTownReply(
        @Header("Authorization") token:String,
        @Path("articleid") articleid:Int,
        @Body body: CommunityTownReplyRequestBody
    ):ApiResult<CommunityTownReplyResponseModel>

    //커뮤니티 동네생활 대댓글 작성
    @POST("/api/comments/{articleid}/{commentid}")
    suspend fun setTownRereply(
        @Header("Authorization") token:String,
        @Path("articleid") articleid:Int,
        @Path("commentid") commentid:Int,
        @Body body: CommunityRereplyRequestBody
    ):ApiResult<CommunityTownReplyResponseModel>

    //커뮤니티 동네생활 게시글 삭제
    @PATCH("/api/articles/{articleid}/delete")
    suspend fun deleteComment(
        @Header("Authorization") token:String,
        @Path("articleid") articleid:Int,
    ):ApiResult<CommunityTownDeleteCommentResponse>

    //커뮤니티 동네생활 댓글 삭제
    @DELETE("/api/comments/{commentid}")
    suspend fun deleteReply(
        @Header("Authorization") token:String,
        @Path("commentid") commentid:Int
    ):ApiResult<CommunityTownReplyDeleteResponse>


    //재난상황 커뮤니티
    @GET("/api/disaster/situation")
    suspend fun getDisasterHome(
        @Header("Authorization") token:String,
    ):ApiResult<CommunityHomeDisasterResponse>

    //재난상황 댓글 모두보기
    @GET("/api/disaster/{sort}/{disasterId}")
    suspend fun getDisasterHomeDetail(
        @Header("Authorization") token:String,
        @Path("disasterId") disasterId:Int,
        @Path("sort") sort:String
    ):ApiResult<CommunityDisasterDetailResponse>
}