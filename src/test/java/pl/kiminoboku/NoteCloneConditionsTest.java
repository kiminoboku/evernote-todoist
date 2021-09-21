package pl.kiminoboku;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteTestObjectsFactory;
import pl.kiminoboku.evernote.NotificationReason;

import static org.assertj.core.api.Assertions.assertThat;

class NoteCloneConditionsTest {
    private static final String CLONED_TAG_NAME = "cloned";

    NoteCloneConditions systemUnderTest = new NoteCloneConditions();

    @ParameterizedTest
    @EnumSource(value = NotificationReason.class, names = {"NOTE_CREATED", "NOTE_UPDATED"})
    void shouldCloneCreatedOrUpdateNotesWithoutClonedTag(NotificationReason notificationReason) {
        EvernoteNote noteWithoutClonedTag = EvernoteTestObjectsFactory.createEvernoteNote();
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification(notificationReason);

        boolean result = systemUnderTest.shouldBeCloned(noteWithoutClonedTag, notification);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = NotificationReason.class, names = {"NOTE_CREATED", "NOTE_UPDATED"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotCloneWhenNotificationReasonIsWrong(NotificationReason notificationReason) {
        EvernoteNote noteWithoutClonedTag = EvernoteTestObjectsFactory.createEvernoteNote();
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification(notificationReason);

        boolean result = systemUnderTest.shouldBeCloned(noteWithoutClonedTag, notification);

        assertThat(result).isFalse();
    }

    @Test
    void shouldNotCloneWhenClonedTagExist() {
        EvernoteNote noteWithClonedTag = EvernoteTestObjectsFactory.createEvernoteNote(CLONED_TAG_NAME);
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification(NotificationReason.NOTE_CREATED);

        boolean result = systemUnderTest.shouldBeCloned(noteWithClonedTag, notification);

        assertThat(result).isFalse();
    }
}