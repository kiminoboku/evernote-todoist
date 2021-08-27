package pl.kiminoboku.spring;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class LambdaLoggerConfiguration {

    private final Context lambdaContext;

    @Bean
    public LambdaLogger lambdaLogger() {
        return lambdaContext.getLogger();
    }
}
