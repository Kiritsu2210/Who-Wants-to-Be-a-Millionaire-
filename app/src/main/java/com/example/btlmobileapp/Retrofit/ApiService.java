package com.example.btlmobileapp.Retrofit;

import com.example.btlmobileapp.Object.CauHoi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/Questions/{Nhom}")
    Call<CauHoi> getQuestion(@Path("Nhom") int Nhom);
}
