package ru.l4legenda.ktk45.api;

import java.util.List;

import retrofit2.http.POST;

public interface ktk {
    @POST("login")
    List<User>
}
