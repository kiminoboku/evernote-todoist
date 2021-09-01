package pl.kiminoboku.evernote;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvernoteTestObjectsFactory {

    public static final String USER_ID = "ANY_USER_ID";

    public static final UUID NOTE_GUID = UUID.randomUUID();
    public static final UUID NOTEBOOK_GUID = UUID.randomUUID();
    public static final String NOTE_TITLE = "ANY_TITLE";
    public static final String NOTE_APP_URL = "ANY_APP_URL";
    public static final String NOTE_WEB_URL = "ANY_WEB_URL";

    public static EvernoteNotification createNotification() {
        return EvernoteNotification.builder()
                .userId(USER_ID)
                .noteGuid(NOTE_GUID)
                .notebookGuid(NOTEBOOK_GUID)
                .notificationReason(NotificationReason.NOTE_CREATED)
                .build();
    }

    public static EvernoteNote createNote() {
        return EvernoteNote.builder()
                .noteGuid(NOTE_GUID)
                .notebookGuid(NOTEBOOK_GUID)
                .title(NOTE_TITLE)
                .appUrl(NOTE_APP_URL)
                .webUrl(NOTE_WEB_URL)
                .tagNames(Collections.emptyList())
                .build();
    }
}
