package pl.kiminoboku.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.Logger;
import pl.kiminoboku.todoist.TodoistNewTaskResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.kiminoboku.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EvernoteServiceTest {
    @InjectMocks
    EvernoteService systemUnderTest;

    @Mock
    NoteStoreClient noteStoreClientMock;
    @Mock
    EvernoteNoteAdapterConverter evernoteNoteAdapterMock;
    @Mock
    Logger loggerMock;

    @Test
    void shouldReturnNote() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        String noteGuid = "a76b8e59-3d60-4acb-85d5-c1fca9e92019";
        Note noteMock = mock(Note.class);
        EvernoteNote evernoteNoteMock = mock(EvernoteNote.class);
        when(noteStoreClientMock.getNote(noteGuid, false, false, false, false))
                .thenReturn(noteMock);
        when(evernoteNoteAdapterMock.from(noteMock)).thenReturn(evernoteNoteMock);

        Option<EvernoteNote> result = systemUnderTest.getNote(UUID.fromString(noteGuid));

        assertThat(result).isDefined();
        assertThat(result.get()).isEqualTo(evernoteNoteMock);
    }

    @Test
    void shouldLogErrorsWhenFindingNote() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        when(noteStoreClientMock.getNote(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenThrow(EDAMNotFoundException.class);

        Option<EvernoteNote> result = systemUnderTest.getNote(UUID.randomUUID());

        verify(loggerMock).log(any(EDAMNotFoundException.class));
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMarkNoteCloned() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        String noteGuid = "a76b8e59-3d60-4acb-85d5-c1fca9e92019";
        Note noteMock = mock(Note.class);
        when(noteStoreClientMock.getNote(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn(noteMock);
        InOrder inOrder = inOrder(noteMock, noteStoreClientMock);

        boolean result = systemUnderTest.markNoteCloned(UUID.fromString(noteGuid), mock(TodoistNewTaskResult.class));

        verify(noteMock).unsetContent();
        verify(noteMock).unsetResources();
        verify(noteMock).addToTagNames("cloned");
        //verify that update note is done at the very end
        inOrder.verify(noteStoreClientMock).updateNote(noteMock);
        inOrder.verifyNoMoreInteractions();
        assertThat(result).isTrue();
    }

    @Test
    void shouldLogErrorsWhenMarkingNoteCloned() throws TException, EDAMNotFoundException, EDAMSystemException, EDAMUserException {
        when(noteStoreClientMock.getNote(anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenThrow(EDAMNotFoundException.class);

        boolean result = systemUnderTest.markNoteCloned(UUID.randomUUID(), mock(TodoistNewTaskResult.class));

        verify(loggerMock).log(any(EDAMNotFoundException.class));
        assertThat(result).isFalse();
    }
}