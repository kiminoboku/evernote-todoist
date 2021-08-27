package pl.kiminoboku.evernote;

import io.vavr.API;
import pl.kiminoboku.todoist.TodoistNewTaskResult;

public class EvernoteService {
    public boolean markNoteCloned(TodoistNewTaskResult createTaskResult) {
        return API.TODO();
    }
}
