package pl.kiminoboku.evernote;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import io.vavr.API;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;

@Component
public class EvernoteNotificationExtractor {
    public Option<EvernoteNotification> getFrom(APIGatewayV2HTTPEvent input) {
        return API.TODO();
    }
}
