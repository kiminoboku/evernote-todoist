package pl.kiminoboku.todoist;

import io.vavr.API;
import io.vavr.control.Option;
import pl.kiminoboku.evernote.EvernoteNotification;

import java.util.List;

public class TodoistRequestCreator {
    public Option<TodoistRequest> requestFor(EvernoteNotification evernoteNotification) {
        return API.TODO();
    }
}
