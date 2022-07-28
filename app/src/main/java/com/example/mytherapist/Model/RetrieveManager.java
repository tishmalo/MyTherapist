package com.example.mytherapist.Model;

import android.content.Context;
import android.widget.Toast;

import com.example.mytherapist.Services.OnFetchDataListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public class RetrieveManager {

    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://mental-health-info-api.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public RetrieveManager(Context context) {
        this.context = context;
    }



    public void getArticles(OnFetchDataListener listener, String article){

        getArticle gA= retrofit.create(RetrieveManager.getArticle.class);

        Call<List<ArticleRetrieved>> call= gA.getTitle();

        call.enqueue(new Callback<List<ArticleRetrieved>>() {
            @Override
            public void onResponse(Call<List<ArticleRetrieved>> call, Response<List<ArticleRetrieved>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "couldn't fetch data", Toast.LENGTH_SHORT).show();
                    return;

                }


            }

            @Override
            public void onFailure(Call<List<ArticleRetrieved>> call, Throwable t) {

            }
        });


    }

    public interface getArticle {

        @Headers({
                "Accept: application/json",
                "X-RapidAPI-Key: 9fa6a918dcmsh7f0406f36768b6ap19199bjsna8ac11f36316",
                "X-RapidAPI-Host: mental-health-info-api.p.rapidapi.com"

        })
        @GET("news/thetimes")
        Call<List<ArticleRetrieved>> getTitle(

        );
    }



}




