package pl.kiminoboku.evernote;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import io.vavr.collection.Array;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class EvernoteNotificationExtractor {
    public Option<EvernoteNotification> getFrom(APIGatewayV2HTTPEvent input) {
        Map<String, String> queryStringParameters = input.getQueryStringParameters();
        if (queryStringParameters != null) {
            Option<String> userIdOption = getUserIdOption(queryStringParameters);
            Option<UUID> noteGuidOption = getNoteGuidOption(queryStringParameters);
            Option<UUID> notebookGuidOption = getNotebookGuid(queryStringParameters);
            Option<NotificationReason> reasonOption = getNotificationReasonOption(queryStringParameters);
            if(allDefined(userIdOption, notebookGuidOption, reasonOption)) {
                return notification(userIdOption, noteGuidOption, notebookGuidOption, reasonOption);
            }
        }
        return Option.none();
    }

    private Option<String> getUserIdOption(Map<String, String> queryStringParameters) {
        return Option.of(queryStringParameters.get("userId"));
    }

    private Option<UUID> getNoteGuidOption(Map<String, String> queryStringParameters) {
        String noteGuid = queryStringParameters.get("guid");
        if (noteGuid != null) {
            return Option.of(UUID.fromString(noteGuid));
        }
        return Option.none();
    }

    private Option<UUID> getNotebookGuid(Map<String, String> queryStringParameters) {
        String notebookGuid = queryStringParameters.get("notebookGuid");
        if(notebookGuid != null) {
            return Option.of(UUID.fromString(notebookGuid));
        }
        return Option.none();
    }

    private Option<NotificationReason> getNotificationReasonOption(Map<String, String> queryStringParameters) {
        return NotificationReason.parseHttpReason(queryStringParameters.get("reason"));
    }

    private boolean allDefined(Option<?> ... options) {
        return Array.of(options).forAll(Option::isDefined);
    }

    private Option<EvernoteNotification> notification(Option<String> userIdOption, Option<UUID> noteGuidOption, Option<UUID> notebookGuidOption, Option<NotificationReason> reasonOption) {
        return Option.of(notification(userIdOption.get(), noteGuidOption.getOrNull(), notebookGuidOption.get(), reasonOption.get()));
    }

    private EvernoteNotification notification(String userId, UUID orNull, UUID notebookGuid, NotificationReason notificationReason) {
        return EvernoteNotification.builder()
                .userId(userId)
                .noteGuid(orNull)
                .notebookGuid(notebookGuid)
                .notificationReason(notificationReason)
                .build();
    }
}
