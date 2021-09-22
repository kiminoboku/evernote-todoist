package pl.kiminoboku;

import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.todoist.TodoistProjectId;

import java.util.Map;
import java.util.UUID;

@Component
public class TodoistProjectMappings {

    Map<UUID, Integer> mappingsConfiguration;

    public TodoistProjectMappings(@Qualifier("mappingsConfiguration") Map<UUID, Integer> mappingsConfiguration) {
        this.mappingsConfiguration = mappingsConfiguration;
    }

    public Option<TodoistProjectId> getTargetProjectIdFor(EvernoteNote evernoteNote) {
        if (mappingsConfiguration.containsKey(evernoteNote.getNotebookGuid())) {
            return Option.of(TodoistProjectId.of(mappingsConfiguration.get(evernoteNote.getNotebookGuid())));
        }
        return Option.none();
    }
}
