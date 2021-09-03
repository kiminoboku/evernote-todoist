package pl.kiminoboku.todoist;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.NoteCloneConditions;
import pl.kiminoboku.TodoistProjectMappings;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteService;

import java.util.UUID;

@Component
@AllArgsConstructor
public class TodoistRequestCreator {

    EvernoteService evernoteService;
    NoteCloneConditions noteCloneConditions;
    TodoistProjectMappings todoistProjectMappings;
    TodoistCreateTaskRequestBuilder requestBuilder;

    public Option<TodoistCreateTaskRequest> requestFor(EvernoteNotification evernoteNotification) {
        Option<UUID> noteGuidOption = evernoteNotification.getNoteGuid();
        if (noteGuidOption.isDefined()) {
            UUID noteGuid = noteGuidOption.get();
            return createIfNoteExists(noteGuid, evernoteNotification);
        }
        return Option.none();
    }

    private Option<TodoistCreateTaskRequest> createIfNoteExists(UUID noteGuid, EvernoteNotification evernoteNotification) {
        Option<EvernoteNote> noteOption = evernoteService.getNote(noteGuid);
        if (noteOption.isDefined()) {
            return createIfCloneConditionsAreMet(noteOption.get(), evernoteNotification);
        }
        return Option.none();
    }

    private Option<TodoistCreateTaskRequest> createIfCloneConditionsAreMet(EvernoteNote note, EvernoteNotification evernoteNotification) {
        boolean shouldBeCloned = noteCloneConditions.shouldBeCloned(note, evernoteNotification);
        if (shouldBeCloned) {
            return createIfTargetProjectIsDefinedFor(note);
        }
        return Option.none();
    }

    private Option<TodoistCreateTaskRequest> createIfTargetProjectIsDefinedFor(EvernoteNote note) {
        Option<TodoistProjectId> targetProjectIdOption = todoistProjectMappings.getTargetProjectIdFor(note);
        if (targetProjectIdOption.isDefined()) {
            return Option.of(buildRequestFrom(note, targetProjectIdOption.get()));
        }
        return Option.none();
    }

    private TodoistCreateTaskRequest buildRequestFrom(EvernoteNote note, TodoistProjectId targetProjectId) {
        return requestBuilder.buildFrom(note, targetProjectId);
    }
}
