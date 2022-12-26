package com.ominfo.staff_original.network;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NetworkAPIServices {

    @POST()
    Observable<JsonElement> login(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> contacts(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> getAppVersion(@Url String url,@Query("jsonreq")  String request);
    @POST()
    Observable<JsonElement> updateKey(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> getAttendanceStaff(@Url String url,@Query("jsonreq")  String request);
    @POST()
    Observable<JsonElement> SingleUser(@Url String url,@Query("jsonreq")  String request);

    @Multipart
    @POST()
    Observable<JsonElement> updateAttendance(@Url String url,
                                             @Part("action") RequestBody uploadType,
                                             @Part("jsonreq") RequestBody uploadTypeImage);

    @Multipart
    @POST()
    Observable<JsonElement> calenderHolidays(@Url String url,
                                             @Part("action") RequestBody uploadType,
                                             @Part("company_id") RequestBody company_id,
                                             @Part("from_date") RequestBody from_date,
                                             @Part("end_date") RequestBody end_date);

    @POST()
    Observable<JsonElement> calenderDetails(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> calenderAll(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> getHighlights(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> fetchKataChitthi(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> fetchLoadingList(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> advanceToDriver(@Url String url,@Query("jsonreq")  String request);

    @POST()
    Observable<JsonElement> getVehicle(@Url String url,@Query("jsonreq")  String request);

    @Multipart
    @POST()
    Observable<JsonElement> saveKataChitthi(@Url String url,
                                            @Part("action") RequestBody uploadType,
                                            @Part("jsonreq") RequestBody uploadTypeImage);

    @Multipart
    @POST()
    Observable<JsonElement> saveLoadingList(@Url String url,
                                            @Part("action") RequestBody uploadType,
                                            @Part("jsonreq") RequestBody uploadTypeImage);

    @GET()
    Observable<JsonElement> dashboard(@Url String url);


    @GET()
    Observable<JsonElement> getBookWithTopic(@Url String url);

    //dummy apis
    @POST()
    Observable<JsonElement> resendOTP(@Url String url);

    @POST()
    Observable<JsonElement> FetchProfileInfo(@Url String url);


    @Multipart
    @POST()
    Observable<JsonElement> updateProfileInfo(@Url String url,
                                              @Part("full_name") RequestBody firstName,
                                              @Part("date_of_birth") RequestBody dob,
                                              @Part("email") RequestBody email,
                                              @Part("mobile") RequestBody phone,
                                              @Part("address") RequestBody address,
                                              @Part("city") RequestBody city,
                                              @Part("state") RequestBody state,
                                              @Part("zip_code") RequestBody zipcode
            , @Part MultipartBody.Part image);


    @Multipart
    @POST()
    Observable<JsonElement> UploadDocToVerification(@Url String url,
                                            @Query("doc_type") RequestBody doc_type
            , @Part MultipartBody.Part front_image, @Part MultipartBody.Part back_image);

    @POST()
    Observable<JsonElement> FetchUserCharge(@Url String url);

    @Multipart
    @POST()
    Observable<JsonElement> UpdateDocToVerification(@Url String url,
                                                    @Part("doc_type") RequestBody doc_type
            , @Part MultipartBody.Part front_image, @Part MultipartBody.Part back_image);


}
