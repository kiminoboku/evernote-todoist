package pl.kiminoboku.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.Logger;
import pl.kiminoboku.todoist.TodoistNewTaskResult;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EvernoteService {

    private static final boolean NO_CONTENT = false;
    private static final boolean NO_RESOURCE_DATA = false;
    private static final boolean NO_RESOURCE_RECOGNITION = false;
    private static final boolean NO_RESOURCE_ALTERNATE_DATA = false;
    public static final String CLONED_TAG_NAME = "cloned";

    NoteStoreClient noteStoreClient;
    EvernoteNoteAdapterConverter converter;
    Logger logger;

    public Option<EvernoteNote> getNote(UUID noteGuid) {
        return Try.of(() -> getNote(noteGuid.toString()))
                .map(converter::from)
                .onFailure(logger::log)
                .toOption();
    }

    public boolean markNoteCloned(UUID noteGuid, TodoistNewTaskResult createTaskResult) {
        return Try.of(() -> getNote(noteGuid.toString()))
                .andThen(Note::unsetContent)
                .andThen(Note::unsetResources)
                .andThen(this::setNoteCloned)
                .mapTry(this::updateNote)
                .onFailure(logger::log)
                .isSuccess();
    }

    private Note getNote(String guid) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
        return noteStoreClient.getNote(guid, NO_CONTENT, NO_RESOURCE_DATA, NO_RESOURCE_RECOGNITION, NO_RESOURCE_ALTERNATE_DATA);
    }

    private void setNoteCloned(Note note) {
        note.addToTagNames(CLONED_TAG_NAME);
    }

    private Note updateNote(Note note) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
        return noteStoreClient.updateNote(note);
    }
}
