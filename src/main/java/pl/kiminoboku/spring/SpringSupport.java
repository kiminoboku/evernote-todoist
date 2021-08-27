package pl.kiminoboku.spring;

import io.vavr.Lazy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringSupport {
    private static final Lazy<GenericApplicationContext> applicationContext = Lazy.of(SpringSupport::createApplicationContext);

    public static GenericApplicationContext getApplicationContext() {
        return applicationContext.get();
    }

    private static GenericApplicationContext createApplicationContext() {
        return new AnnotationConfigApplicationContext("pl");
    }
}
