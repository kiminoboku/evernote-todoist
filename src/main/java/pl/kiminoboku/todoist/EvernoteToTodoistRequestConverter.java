package pl.kiminoboku.todoist;

import io.vavr.API;
import pl.kiminoboku.evernote.EvernoteNote;

public class EvernoteToTodoistRequestConverter {
    public TodoistCreateTaskRequest convert(EvernoteNote evernoteNote, TodoistProjectId targetProjectId) {
        /*
         * Create task request:
         * 1. Title copied from note
         * 2. Description linking to note (Webapp & Android App)
         * 3. Target project id set
         */
        return API.TODO();
    }
}
