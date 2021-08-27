package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteNotificationExtractor;
import pl.kiminoboku.exception.ThrowableLogger;
import pl.kiminoboku.todoist.TodoistRequestCreator;
import pl.kiminoboku.todoist.TodoistService;

@Component
@AllArgsConstructor
public class EvernoteNotificationHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static final int HTTP_OK_200 = 200;

    private final ThrowableLogger throwableLogger;
    private final EvernoteNotificationExtractor notificationExtractor;
    private final TodoistRequestCreator todoistRequestCreator;
    private final TodoistService todoistService;

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        Try.run(() -> {
            //get valid inputs option
            Option<EvernoteNotification> notificationOption = getNotificationOption(input);
            if(notificationOption.isDefined()) {
                EvernoteNotification notification = notificationOption.get();
                //determine if todoist needs to be involved
                todoistRequestCreator.requestFor(notification);
                //run todoist

                //update notes with "cloned" tag

            }
        }).onFailure(throwableLogger::log);

        //return 200
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HTTP_OK_200)
                .build();
    }

    private Option<EvernoteNotification> getNotificationOption(APIGatewayV2HTTPEvent input) {
        return notificationExtractor.getFrom(input);
    }
}
