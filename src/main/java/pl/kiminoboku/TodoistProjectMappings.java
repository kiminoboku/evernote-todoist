package pl.kiminoboku;

import io.vavr.API;
import io.vavr.control.Option;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.todoist.TodoistProjectId;

public class TodoistProjectMappings {
    public Option<TodoistProjectId> getTargetProjectIdFor(EvernoteNote evernoteNote) {
        return API.TODO();
    }
}
