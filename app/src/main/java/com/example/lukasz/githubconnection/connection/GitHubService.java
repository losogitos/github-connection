package com.example.lukasz.githubconnection.connection;

import com.example.lukasz.githubconnection.model.Repo;
import com.example.lukasz.githubconnection.model.User;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Łukasz Ciupa on 28.04.17.
 */

public interface GitHubService {

    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user);

    @GET("user")
    Observable<User> login(@Header("Authorization") String credentials);
}
