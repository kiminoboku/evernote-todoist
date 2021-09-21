package pl.kiminoboku;

import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.NotificationReason;

import java.util.EnumSet;

public class NoteCloneConditions {
    public boolean shouldBeCloned(EvernoteNote note, EvernoteNotification notification) {
        return satisfyCloneConditions(note, notification);
    }

    private boolean satisfyCloneConditions(EvernoteNote note, EvernoteNotification notification) {
        return doesNotHaveClonedTag(note) && isNoteCreatedOrUpdated(notification);
    }

    private boolean doesNotHaveClonedTag(EvernoteNote note) {
        return !note.getTagNames().contains("cloned");
    }

    private boolean isNoteCreatedOrUpdated(EvernoteNotification notification) {
        return EnumSet.of(NotificationReason.NOTE_CREATED, NotificationReason.NOTE_UPDATED)
                .contains(notification.getNotificationReason());
    }
}
