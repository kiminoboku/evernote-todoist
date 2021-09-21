package pl.kiminoboku.todoist.retrofit;

import lombok.AllArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class OkHttpClientConfiguration {

    @Value("#{environment.TODOIST_API_TOKEN}")
    private String bearerToken;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(this::bearerTokenInterceptor)
                .build();
    }

    private Response bearerTokenInterceptor(Interceptor.Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        Request request = requestBuilder
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();
        return chain.proceed(request);
    }
}
