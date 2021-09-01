package pl.kiminoboku;

import io.vavr.API;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteNotification;

public class NoteCloneConditions {
    public boolean shouldBeCloned(EvernoteNote note, EvernoteNotification notification) {
        /*
         * Note should be cloned as task for following conditions:
         * - No "cloned" tag is attached to note
         * - Notification reason is either note created or note updated
         */
        return API.TODO();
    }
}
