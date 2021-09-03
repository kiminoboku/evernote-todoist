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
        Option<Map<String, String>> notificationParametersOption = getQueryParameters(input);
        if (notificationParametersOption.isDefined()) {
            return getFrom(notificationParametersOption.get());
        }
        return Option.none();
    }

    private Option<Map<String, String>> getQueryParameters(APIGatewayV2HTTPEvent input) {
        return Option.of(input.getQueryStringParameters());
    }

    private Option<EvernoteNotification> getFrom(Map<String, String> notificationParameters) {
        Option<String> userIdOption = getUserIdOption(notificationParameters);
        Option<UUID> noteGuidOption = getNoteGuidOption(notificationParameters);
        Option<UUID> notebookGuidOption = getNotebookGuid(notificationParameters);
        Option<NotificationReason> reasonOption = getNotificationReasonOption(notificationParameters);
        if (allDefined(userIdOption, notebookGuidOption, reasonOption)) {
            return notification(userIdOption, noteGuidOption, notebookGuidOption, reasonOption);
        }
        return Option.none();
    }

    private Option<String> getUserIdOption(Map<String, String> notificationParameters) {
        return Option.of(notificationParameters.get("userId"));
    }

    private Option<UUID> getNoteGuidOption(Map<String, String> notificationParameters) {
        String noteGuid = notificationParameters.get("guid");
        if (noteGuid != null) {
            return Option.of(UUID.fromString(noteGuid));
        }
        return Option.none();
    }

    private Option<UUID> getNotebookGuid(Map<String, String> notificationParameters) {
        String notebookGuid = notificationParameters.get("notebookGuid");
        if (notebookGuid != null) {
            return Option.of(UUID.fromString(notebookGuid));
        }
        return Option.none();
    }

    private Option<NotificationReason> getNotificationReasonOption(Map<String, String> notificationParameters) {
        return NotificationReason.parseHttpReason(notificationParameters.get("reason"));
    }

    private boolean allDefined(Option<?>... options) {
        return Array.of(options).forAll(Option::isDefined);
    }

    private Option<EvernoteNotification> notification(Option<String> userIdOption, Option<UUID> noteGuidOption, Option<UUID> notebookGuidOption, Option<NotificationReason> reasonOption) {
        return Option.of(notification(userIdOption.get(), noteGuidOption.getOrNull(), notebookGuidOption.get(), reasonOption.get()));
    }

    private EvernoteNotification notification(String userId, UUID noteGuid, UUID notebookGuid, NotificationReason notificationReason) {
        return EvernoteNotification.builder()
                .userId(userId)
                .noteGuid(noteGuid)
                .notebookGuid(notebookGuid)
                .notificationReason(notificationReason)
                .build();
    }
}
