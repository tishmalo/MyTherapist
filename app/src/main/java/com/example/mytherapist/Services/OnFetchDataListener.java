package com.example.mytherapist.Services;

import com.example.mytherapist.Model.ArticleRetrieved;

import java.util.List;

public interface OnFetchDataListener {

    void OnFetch(ArticleRetrieved response);
    void onFetch(String message);


}
