package ru.l4legenda.ktk45.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface UserApi {

    @POST("login")
    Call<List<User>> users();

}
