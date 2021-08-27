package pl.kiminoboku.evernote;

import io.vavr.API;
import org.springframework.stereotype.Component;
import pl.kiminoboku.todoist.TodoistNewTaskResult;

@Component
public class EvernoteService {
    public boolean markNoteCloned(TodoistNewTaskResult createTaskResult) {
        return API.TODO();
    }
}
