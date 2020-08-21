package com.character.microblogapp.net;

import com.character.microblogapp.model.MAlarm;
import com.character.microblogapp.model.MAppAlarm;
import com.character.microblogapp.model.MAreaList;
import com.character.microblogapp.model.MBanner;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MBlock;
import com.character.microblogapp.model.MCharacter;
import com.character.microblogapp.model.MCharacterInfo;
import com.character.microblogapp.model.MCharacterInfo2;
import com.character.microblogapp.model.MChat;
import com.character.microblogapp.model.MChatRoom;
import com.character.microblogapp.model.MComment;
import com.character.microblogapp.model.MCommunity;
import com.character.microblogapp.model.MCommunityInfo;
import com.character.microblogapp.model.MContent;
import com.character.microblogapp.model.MCurrentEstimateUser;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MEstimateResult;
import com.character.microblogapp.model.MEvent;
import com.character.microblogapp.model.MFindEmail;
import com.character.microblogapp.model.MFindPwd;
import com.character.microblogapp.model.MInterest;
import com.character.microblogapp.model.MLeftEnergy;
import com.character.microblogapp.model.MQna;
import com.character.microblogapp.model.MRefreshTodayMetting;
import com.character.microblogapp.model.MSignup;
import com.character.microblogapp.model.MUploadFile;
import com.character.microblogapp.model.MUploadFiles;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.model.MUserCharacterInfo;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.model.MVersion;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @POST("v1/nid/me")
    Call<ResponseBody> getNaverProfile(@Header("Authorization") String token);

    @Multipart
    @POST("./UploadImage")
    Call<MUploadFile> uploadFile(@Part MultipartBody.Part[] file);

    @Multipart
    @POST("./UploadImages")
    Call<MUploadFiles> uploadFiles(@Part MultipartBody.Part[] file);

    @FormUrlEncoded
    @POST("./Login/login")
    Call<MUser> login(@Field("email") String email,
                      @Field("pwd") String pwd,
                      @Field("login_type") int login_type,
                      @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("./Login/find_email")
    Call<MFindEmail> find_email(@Field("name") String name,
                                @Field("birthday") String birthday);

    @FormUrlEncoded
    @POST("./Login/find_pwd")
        ////add api by tester
    Call<MFindPwd> find_pwd(@Field("name") String name,
                            @Field("birthday") String birthday);

    @FormUrlEncoded
    @POST("./Login/check_email_dup")
    Call<MBase> check_email_dup(@Field("email") String email);

    @FormUrlEncoded
    @POST("./Login/check_phone_dup")
    Call<MBase> check_phone_dup(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("./Login/signup")
    Call<MSignup> signup(@Field("login_type") int login_type,
                         @Field("sns_id") String sns_id,
                         @Field("email") String email,
                         @Field("email_type") String email_type,
                         @Field("pwd") String pwd,
                         @Field("gender") int gender,
                         @Field("nickname") String nickname,
                         @Field("height") int height,
                         @Field("age") String age,
                         @Field("profile") String profile,
                         @Field("address") String address,
                         @Field("school") String school,
                         @Field("job") String job,
                         @Field("religion") String religion,
                         @Field("drink") String drink,
                         @Field("smoke") String smoke,
                         @Field("body_type") String body_type,
                         @Field("interest") String interest,
                         @Field("love_style") String love_style,
                         @Field("intro") String intro,
                         @Field("phone") String phone,
                         @Field("birthday") String birthday,
                         @Field("latitude") double latitude,
                         @Field("longitude") double longitude,
                         @Field("fcm_token") String fcm_token,
                         @Field("cer_name") String cer_name);

    @FormUrlEncoded
    @POST("./Extra/add_qna")
    Call<MBase> add_qna(@Field("usr_uid") int usr_uid,
                        @Field("type") String type,
                        @Field("title") String title,
                        @Field("content") String content,
                        @Field("images") String images
    );

    @FormUrlEncoded
    @POST("./Extra/get_qna_list")
    Call<MQna> get_qna_list(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./Extra/get_qna_detail_info")
    Call<MQna> get_qna_detail_info(@Field("qna_uid") int qna_uid);

    @FormUrlEncoded
    @POST("./Extra/get_agreement")
    Call<MContent> get_agreement(@Field("type") int type);

    @FormUrlEncoded
    @POST("./Extra/get_alarm_list")
    Call<MAlarm> get_alarm_list(@Field("usr_uid") int usr_uid,
                                @Field("page_num") int page_num);

    @FormUrlEncoded
    @POST("./User/get_my_energy")
    Call<MEnergy> get_my_energy(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/restore_my_energy")
    Call<MEnergy> restore_my_energy(@Field("usr_uid") int usr_uid,
                                    @Field("type") int type);

    @FormUrlEncoded
    @POST("./User/purchase_energy")
    Call<MBase> purchase_energy(@Field("usr_uid") int usr_uid,
                                @Field("pay_uid") String pay_uid,
                                @Field("purchase_type") int purchase_type);

    @FormUrlEncoded
    @POST("./User/get_energy_usehis")
    Call<MEnergy> get_energy_usehis(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/signout")
    Call<MBase> signout(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/change_alarm_setting")
    Call<MBase> change_alarm_setting(@Field("usr_uid") int usr_uid,
                                     @Field("type") int type,
                                     @Field("status") int status);

    @FormUrlEncoded
    @POST("./User/get_block_user_list")
    Call<MBlock> get_block_user_list(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/set_block_user")
    Call<MBase> set_block_user(@Field("usr_uid") int usr_uid,
                               @Field("phone") String phone,
                               @Field("status") int status);

    @FormUrlEncoded
    @POST("./Extra/get_notice_Event_list")
    Call<MEvent> get_notice_Event_list(@Field("page_num") int page_num);

    @POST("./Extra/banner_list")
    Call<MBanner> get_banner_list();

    @FormUrlEncoded
    @POST("./User/change_usr_pwd")
    Call<MBase> change_usr_pwd(@Field("usr_uid") int usr_uid,
                               @Field("old_pwd") String old_pwd,
                               @Field("new_pwd") String new_pwd);

    @FormUrlEncoded
    @POST("./User/set_ideal_setting")
    Call<MBase> set_ideal_setting(@Field("usr_uid") int usr_uid,
                                  @Field("start_height") int start_height,
                                  @Field("end_height") int end_height,
                                  @Field("address") String address,
                                  @Field("religion") String religion,
                                  @Field("smoke") String smoke,
                                  @Field("drink") String drink,
                                  @Field("body_type") String body_type,
                                  @Field("interest") String interest,
                                  @Field("love_style") String love_style);

    @FormUrlEncoded
    @POST("./User/get_ideal_info")
    Call<MUser> get_ideal_info(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/update_profile")
    Call<MBase> update_profile(@Field("usr_uid") int usr_uid,
                               @Field("nickname") String nickname,
                               @Field("height") int height,
                               @Field("profile") String profile,//
                               @Field("address") String address,
                               @Field("school") String school,
                               @Field("job") String job,
                               @Field("religion") String religion,
                               @Field("drink") String drink,
                               @Field("smoke") String smoke,
                               @Field("body_type") String body_type,
                               @Field("interest") String interest,
                               @Field("love_style") String love_style,
                               @Field("intro") String intro);

    @FormUrlEncoded
    @POST("./User/get_my_profile_info")
    Call<MUser> get_my_profile_info(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./Community/add_community")
    Call<MBase> add_community(@Field("usr_uid") int usr_uid,
                              @Field("community_uid") int community_uid,
                              @Field("type") int type,
                              @Field("title") String title,
                              @Field("content") String content,
                              @Field("add_file") String add_file);

    @FormUrlEncoded
    @POST("./Community/add_community_comment")
    Call<MBase> add_community_comment(@Field("usr_uid") int usr_uid,
                                      @Field("community_uid") int community_uid,
                                      @Field("content") String content,
                                      @Field("parent_uid") int parent_uid);

    @FormUrlEncoded
    @POST("./Community/remove_community")
    Call<MBase> remove_community(@Field("usr_uid") int usr_uid,
                                 @Field("community_uid") int community_uid);

    @FormUrlEncoded
    @POST("./Community/like_community")
    Call<MBase> like_community(@Field("usr_uid") int usr_uid,
                               @Field("community_uid") int community_uid);

    @FormUrlEncoded
    @POST("./Community/like_community_comment")
    Call<MBase> like_community_comment(@Field("usr_uid") int usr_uid,
                                       @Field("community_comment_uid") int community_comment_uid);

    @FormUrlEncoded
    @POST("./Community/get_community_detail_info")
    Call<MCommunityInfo> get_community_detail_info(@Field("usr_uid") int usr_uid,
                                                   @Field("community_uid") int community_uid);

    @FormUrlEncoded
    @POST("./Community/add_report_community")
    Call<MBase> add_report_community(@Field("usr_uid") int usr_uid,
                                     @Field("community_uid") int community_uid);

    @FormUrlEncoded
    @POST("./Community/get_community_comment_list")
    Call<MComment> get_community_comment_list(@Field("usr_uid") int usr_uid,
                                              @Field("community_uid") int community_uid);

    @FormUrlEncoded
    @POST("./Community/get_community_list")
    Call<MCommunity> get_community_list(@Field("usr_uid") int usr_uid,
                                        @Field("type") int type,
                                        @Field("page_num") int page_num,
                                        @Field("search_key") String search_key);

    @FormUrlEncoded
    @POST("./Community/remove_community_comment")
    Call<MBase> remove_community_comment(@Field("usr_uid") int usr_uid,
                                         @Field("community_uid") int community_id,
                                         @Field("comment_uid") int comment_uid);

    @FormUrlEncoded
    @POST("./Chat/get_count")
    Call<MBase> get_count(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./Chat/get_chat_list")
    Call<MChat> get_chat_list(@Field("usr_uid") int usr_uid,
                              @Field("chat_room_uid") int chat_room_uid,
                              @Field("other_usr_uid") int other_usr_uid,
                              @Field("page_num") int page_num);

    @FormUrlEncoded
    @POST("./Chat/report_user")
    Call<MBase> report_user(@Field("usr_uid") int usr_uid,
                            @Field("other_usr_uid") int other_usr_uid,
                            @Field("reason") String reason);

    @FormUrlEncoded
    @POST("./Chat/remove_chat_room")
    Call<MBase> remove_chat_room(@Field("usr_uid") int usr_uid,
                                 @Field("chat_room_uid") int chat_room_uid);

    @FormUrlEncoded
    @POST("./Chat/open_chat_room")
    Call<MLeftEnergy> open_chat_room(@Field("usr_uid") int usr_uid,
                                     @Field("chat_room_uid") int chat_room_uid,
                                     @Field("other_uid") int other_uid);

    @FormUrlEncoded
    @POST("./Chat/get_chat_room_list")
    Call<MChatRoom> get_chat_room_list(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./Chat/process_chat_room")
    Call<MBase> process_chat_room(
            @Field("usr_uid") int usr_uid,
            @Field("room_uid") int room_uid,
            @Field("action") String action);

    @FormUrlEncoded
    @POST("./Chat/send_chat")
    Call<MBase> send_chat(@Field("usr_uid") int usr_uid,
                          @Field("chat_room_uid") int chat_room_uid,
                          @Field("other_usr_uid") int other_usr_uid,
                          @Field("type") int type,
                          @Field("content") String content,
                          @Field("add_file") String add_file);

    /**
     * 현재 평가회원얻기
     */
    @FormUrlEncoded
    @POST("./Estimate/get_current_estimate_usr")
    Call<MCurrentEstimateUser> get_current_estimate_usr(@Field("usr_uid") int usr_uid);

    /**
     * 평가탭 회원평가
     */
    @FormUrlEncoded
    @POST("./Estimate/estimate_current_usr")
    Call<MEstimateResult> estimate_current_usr(
            @Field("usr_uid") int usr_uid,
            @Field("target_usr_uid") int target_usr_uid,
            @Field("score") int score
    );

    /**
     * 회원평가
     */
    @FormUrlEncoded
    @POST("./User/estimate_usr")
    Call<MBase> estimate_usr(
            @Field("usr_uid") int usr_uid,
            @Field("target_usr_uid") int target_usr_uid,
            @Field("score") int score
    );

    /**
     * 호감표시 받은 회원목록얻기
     */
    @FormUrlEncoded
    @POST("./History/get_heart_receive_usr")
    Call<MUserList> get_heart_receive_usr(@Field("usr_uid") int usr_uid, @Field("page_num") int page_num);

    /**
     * 호감표시한 회원목록
     */
    @FormUrlEncoded
    @POST("./History/get_hearted_usr")
    Call<MUserList> get_hearted_usr(@Field("usr_uid") int usr_uid, @Field("page_num") int page_num);

    /**
     * 높은점수 받은 회원목록
     */
    @FormUrlEncoded
    @POST("./History/get_high_score_receive_usr")
    Call<MUserList> get_high_score_receive_usr(@Field("usr_uid") int usr_uid, @Field("page_num") int page_num);

    /**
     * 높은점수 보낸 회원목록
     */
    @FormUrlEncoded
    @POST("./History/get_high_scored_usr")
    Call<MUserList> get_high_scored_usr(@Field("usr_uid") int usr_uid, @Field("page_num") int page_num);

    /**
     * 오늘의 만남얻기
     */
    @FormUrlEncoded
    @POST("./Main/get_today_meeting")
    Call<MUserList> get_today_meeting(@Field("usr_uid") int usr_uid);

    /**
     * 읽지 않은 알람갯수 얻기
     */
    @FormUrlEncoded
    @POST("./Main/get_unread_alarm_cnt")
    Call<MBase> get_unread_alarm_cnt(@Field("usr_uid") int usr_uid);

    /**
     * 이전 만남이력얻기
     */
    @FormUrlEncoded
    @POST("./Main/get_old_meeting")
    Call<MUserList> get_old_meeting(@Field("usr_uid") int usr_uid);

    /**
     * 오늘의 만남 2명유저 선택
     */
    @FormUrlEncoded
    @POST("./Main/select_two_usr_today_meeting")
    Call<MBase> select_two_usr_today_meeting(
            @Field("usr_uid") int usr_uid,
            @Field("target_usr_uid1") int target_usr_uid1,
            @Field("target_usr_uid2") int target_usr_uid2
    );

    /**
     * 오늘의 만남 1명유저 선택
     */
    @FormUrlEncoded
    @POST("./Main/select_one_usr_today_meeting")
    Call<MBase> select_one_usr_today_meeting(
            @Field("usr_uid") int usr_uid,
            @Field("target_usr_uid") int target_usr_uid
    );

    /**
     * 오늘의 만남 2명유저 선택한 다음 취소하고 다시 선택하려고 함. //에너지 15개 감소!
     */
    @FormUrlEncoded
    @POST("./Main/refresh_today_meeting")
    Call<MRefreshTodayMetting> refresh_today_meeting(
            @Field("usr_uid") int usr_uid
    );

    /**
     * 이상형 이성 목록 얻기
     */
    @FormUrlEncoded
    @POST("./Main/get_model_find")
    //option은 type마다 다르다. 키도 String으로 올려보낸다. 서버에서 변환할것.
    //type: 1:상위20%, 2: 선호하는 성격, 3: 체형, 4: 뉴페이스, 5: 동네 친구, 6: 지금 접속중, 7: 종교, 8: 음주습관, 9: 키, 10: 관심사, 11: 직업, 12: 연애스타일
    //선호하는 성격 얻을때  data1이 null값일수도 있음(성격이 한글자짜리일때 data2만 값이 있음.)
    //체형 얻을때 data2이 null값일수 있음.
    Call<MUserList> get_model_find(
            @Field("usr_uid") int usr_uid,
            @Field("type") int type,
            @Field("option1") String option1,
            @Field("option2") String option2,
            @Field("option3") String option3
    );

    @FormUrlEncoded
    @POST("./Extra/get_app_alarm")
    Call<MAppAlarm> get_app_alarm(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/logout")
    Call<MBase> logout(@Field("usr_uid") int usr_uid);

    @FormUrlEncoded
    @POST("./User/get_user_profile_info")
        // 다른 사용자의 정보얻기
    Call<MUser> get_user_profile_info(@Field("usr_uid") int usr_uid, @Field("other_usr_uid") int other_usr_uid);

    @FormUrlEncoded
    @POST("./User/like_user")
        //다른 사용자 좋아하기
    Call<MBase> like_user(@Field("usr_uid") int usr_uid,
                          @Field("other_usr_uid") int other_usr_uid);

    @FormUrlEncoded
    @POST("./User/cancel_like")
    Call<MBase> cancel_like(@Field("usr_uid") int usr_uid,
                            @Field("other_id") int other_usr_uid);

    /**
     * 지역리스트얻기
     */
    @FormUrlEncoded
    @POST("./Extra/get_region")
    Call<MAreaList> get_region(@Field("town") String town);

    /**
     * 지역리스트얻기
     */
    @GET("./Extra/get_area")
    Call<MAreaList> get_area();

    /**
     * 관심사얻기
     */
    @FormUrlEncoded
    @POST("./Extra/get_interest")
    Call<MInterest> get_interest(@Field("usr_uid") int usr_uid);

    /**
     * 성격테스트질문리스트얻기기
     */
    @GET("./Extra/get_character_quiz_list")
    Call<MCharacter> get_character_quiz_list();

    /**
     * 성격테스트저장
     */
    @FormUrlEncoded
    @POST("./User/save_character_info")
    Call<MCharacterInfo> save_character_info(
            @Field("usr_uid") int usr_uid,
            @Field("character") String character
    );

    /**
     * 성격정보얻기
     */
    @FormUrlEncoded
    @POST("./User/get_character_info_style")
    Call<MVersion> get_character_info_style(
            @Field("character") String character
    );
    @FormUrlEncoded
    @POST("./User/get_character_info_style2")
    Call<MCharacterInfo2> get_character_info_style2(
            @Field("character") String character
    );

    /**
     * 소통방법 얻기
     */
    @FormUrlEncoded
    @POST("./User/get_character_chat_method")
    Call<MVersion> get_character_chat_method(
            @Field("character") String character
    );

    /**
     * 에너지사용
     */
    @FormUrlEncoded
    @POST("./User/calc_point")
    Call<MEnergy> calc_point(
            @Field("usr_uid") int usr_uid,
            @Field("count") int count,
            @Field("reason") String reason
    );

    /**
     * 성격테스트저장
     */
    @FormUrlEncoded
    @POST("./User/get_character_info")
    Call<MUserCharacterInfo> get_character_info(
            @Field("usr_uid") int usr_uid,
            @Field("other_uid") int other_uid
    );

    @FormUrlEncoded
    @POST("./User/get_user_character_info")
        // 다른 사용자의 정보얻기
    Call<MUser> get_user_character_info(@Field("usr_uid") int usr_uid, @Field("type") String type, @Field("status") int status);

    @POST("./User/get_disc_info")
        // 다른 사용자의 정보얻기
    Call<MVersion> get_disc_info();

    @FormUrlEncoded
    @POST("./User/show_notice")
        //공시 보기 이력 추가
    Call<MBase> show_notice(@Field("usr_uid") int usr_uid,
                          @Field("notice_uid") int notice_uid);
}
