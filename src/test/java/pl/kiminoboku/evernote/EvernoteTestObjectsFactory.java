package pl.kiminoboku.evernote;

import com.evernote.edam.type.Note;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EvernoteTestObjectsFactory {

    public static final String USER_ID = "ANY_USER_ID";

    public static final UUID NOTE_GUID = UUID.randomUUID();
    public static final UUID NOTEBOOK_GUID = UUID.randomUUID();
    public static final String NOTE_TITLE = "ANY_TITLE";

    public static EvernoteNotification createNotification() {
        return createNotification(NotificationReason.NOTE_CREATED);
    }

    public static EvernoteNotification createNotification(NotificationReason notificationReason) {
        return EvernoteNotification.builder()
                .userId(USER_ID)
                .noteGuid(NOTE_GUID)
                .notebookGuid(NOTEBOOK_GUID)
                .notificationReason(notificationReason)
                .build();
    }

    public static EvernoteNote createEvernoteNote(String... tagNames) {
        return EvernoteNote.builder()
                .noteGuid(NOTE_GUID)
                .notebookGuid(NOTEBOOK_GUID)
                .title(NOTE_TITLE)
                .tagNames(Arrays.asList(tagNames))
                .build();
    }

    public static Note createNote() {
        Note note = new Note();
        note.setGuid(NOTE_GUID.toString());
        note.setTitle(NOTE_TITLE);
        note.setNotebookGuid(NOTEBOOK_GUID.toString());
        return note;
    }
}
