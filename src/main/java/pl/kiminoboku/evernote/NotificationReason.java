package pl.kiminoboku.evernote;

import io.vavr.collection.Array;
import io.vavr.control.Option;

public enum NotificationReason {
    NOTEBOOK_CREATED("notebook_create"),
    NOTEBOOK_UPDATED("notebook_update"),
    NOTE_CREATED("create"),
    NOTE_UPDATED("update");

    private final String httpReason;

    NotificationReason(String httpReason) {
        this.httpReason = httpReason;
    }

    public static Option<NotificationReason> parseHttpReason(String httpReason) {
        return Array.of(values()).find(notificationReason -> notificationReason.httpReason.equals(httpReason));
    }
}
