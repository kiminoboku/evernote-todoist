package pl.kiminoboku.todoist;

import io.vavr.API;
import io.vavr.control.Option;
import org.springframework.stereotype.Component;
import pl.kiminoboku.evernote.EvernoteNotification;

@Component
public class TodoistRequestCreator {
    public Option<TodoistCreateTaskRequest> requestFor(EvernoteNotification evernoteNotification) {
        return API.TODO();
    }
}
