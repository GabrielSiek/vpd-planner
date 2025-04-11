package com.vpd.Tmdb;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TmdbService {

    private final OkHttpClient client = new OkHttpClient();

    @Value("${tmdb.api.token}")
    private String token;

    @Value("${tmdb.api.url}")
    private String baseUrl;

    public String searchMovie(String query, String pagina) throws IOException {
        HttpUrl url = HttpUrl.parse(baseUrl + "/3/search/movie").newBuilder()
                .addQueryParameter("query", query)
                .addQueryParameter("include_adult", "false")
                .addQueryParameter("language", "pt-BR")
                .addQueryParameter("page", pagina)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Error on requisition: " + response);
            }

            assert response.body() != null;
            return response.body().string();
        }
    }
}
