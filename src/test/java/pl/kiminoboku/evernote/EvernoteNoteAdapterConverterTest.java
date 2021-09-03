package pl.kiminoboku.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.Logger;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvernoteNoteAdapterConverterTest {
    @InjectMocks
    EvernoteNoteAdapterConverter systemUnderTest;

    @Mock
    NoteStoreClient noteStoreClientMock;
    @Mock
    Logger loggerMock;

    @Test
    void shouldConvertNote() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        Note note = EvernoteTestObjectsFactory.createNote();
        List<String> expectedTags = List.of("tag1", "tag2");
        when(noteStoreClientMock.getNoteTagNames(note.getGuid())).thenReturn(expectedTags);

        EvernoteNote result = systemUnderTest.from(note);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(EvernoteNote.builder()
                .noteGuid(EvernoteTestObjectsFactory.NOTE_GUID)
                .notebookGuid(EvernoteTestObjectsFactory.NOTEBOOK_GUID)
                .tagNames(expectedTags)
                .title(EvernoteTestObjectsFactory.NOTE_TITLE)
                .build());
    }

    @Test
    void shouldLogErrors() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        Note note = EvernoteTestObjectsFactory.createNote();
        when(noteStoreClientMock.getNoteTagNames(note.getGuid())).thenThrow(EDAMNotFoundException.class);

        systemUnderTest.from(note);

        verify(loggerMock).log(any(EDAMNotFoundException.class));
    }
}