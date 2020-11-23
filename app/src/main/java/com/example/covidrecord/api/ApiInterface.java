package com.example.covidrecord.api;

import com.example.covidrecord.CovidList;


import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.GET;


public interface ApiInterface {
    @GET("dayone/country/india")
    Observable<List<CovidList>> getRecordList();



}
