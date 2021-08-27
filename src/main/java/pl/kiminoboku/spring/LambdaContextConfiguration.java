package pl.kiminoboku.spring;

import com.amazonaws.services.lambda.runtime.Context;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaContextConfiguration {

    private static Context context;

    public static void setContext(Context context) {
        LambdaContextConfiguration.context = context;
    }

    @Bean
    public Context lambdaContext() {
        return context;
    }
}
