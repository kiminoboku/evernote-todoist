package pl.kiminoboku.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.Logger;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class EvernoteNoteAdapterConverter {

    NoteStoreClient noteStoreClient;
    Logger logger;

    public EvernoteNote from(Note note) {
        return EvernoteNote.builder()
                .noteGuid(UUID.fromString(note.getGuid()))
                .notebookGuid(UUID.fromString(note.getNotebookGuid()))
                .title(note.getTitle())
                .tagNames(getTagNames(note))
                .build();
    }

    private List<String> getTagNames(Note note) {
        return Try.of(() -> noteStoreClient.getNoteTagNames(note.getGuid()))
                .onFailure(logger::log)
                .getOrElse(Collections.emptyList());
    }
}