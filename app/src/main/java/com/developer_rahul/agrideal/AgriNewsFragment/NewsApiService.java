package com.developer_rahul.agrideal.AgriNewsFragment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/everything")
    Call<NewsResponse> getAgricultureNews(
            @Query("q") String keyword,
            @Query("apiKey") String apiKey
    );
}