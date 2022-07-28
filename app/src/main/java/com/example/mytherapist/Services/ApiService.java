package com.example.mytherapist.Services;

import com.example.mytherapist.Model.ArticleRetrieved;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {


    @GET("/news/thetimes")
    Call<List<ArticleRetrieved>> getArticles();

}
