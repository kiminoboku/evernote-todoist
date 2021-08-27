package pl.kiminoboku.todoist;

import io.vavr.API;
import io.vavr.control.Option;
import pl.kiminoboku.evernote.EvernoteNotification;

public class TodoistRequestCreator {
    public Option<TodoistCreateTaskRequest> requestFor(EvernoteNotification evernoteNotification) {
        return API.TODO();
    }
}
