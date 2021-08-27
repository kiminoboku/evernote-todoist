package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

@Component
@AllArgsConstructor
public class Logger {

    private final LambdaLogger logger;

    public void log(Throwable throwable) {
        logger.log(stackFrom(throwable));
    }

    public void log(String pattern, Object... arguments) {
        String message = MessageFormat.format(pattern, arguments);
        logger.log(message);
    }

    private String stackFrom(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }
}
