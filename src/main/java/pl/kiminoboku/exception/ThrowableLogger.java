package pl.kiminoboku.exception;

import io.vavr.API;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class ThrowableLogger {
    private String stackFrom(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    public void log(Throwable throwable) {
        API.TODO();
    }
}
