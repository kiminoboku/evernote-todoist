package pl.kiminoboku.todoist.retrofit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class TodoistRetrofitConfiguration {

    private final Retrofit retrofit;

    TodoistRetrofitConfiguration() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.todoist.com/rest/")
                .build();
    }

    @Bean
    public TodoistRetrofitService todoistRetrofitService() {
        return retrofit.create(TodoistRetrofitService.class);
    }
}
