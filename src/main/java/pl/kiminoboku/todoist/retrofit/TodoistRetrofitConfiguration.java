package pl.kiminoboku.todoist.retrofit;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class TodoistRetrofitConfiguration {

    private final Retrofit retrofit;

    TodoistRetrofitConfiguration(OkHttpClient client) {
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.todoist.com/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public TodoistRetrofitService todoistRetrofitService() {
        return retrofit.create(TodoistRetrofitService.class);
    }
}
