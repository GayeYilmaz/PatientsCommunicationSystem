package com.example.patientscomm.Fragments;

import com.example.patientscomm.Notifications.MyResponse;
import com.example.patientscomm.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                     "Authorization:key=AAAA36J0vus:APA91bHXkOh8sEAJqigtE_ovr2KS3lRVMjj7nuMRag11tCRbjv6fQmHiGnV9X_DS0MMxBu8hGwQg9WHTMkgQg68xGRWsqChn3jf5P1cTsxK4Mvh6Z7q1Ve4W-SYTd2c8Ib-A03bnVm1f"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body) ;


}
