package pl.kiminoboku.todoist;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.NoteCloneConditions;
import pl.kiminoboku.TodoistProjectMappings;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteService;
import pl.kiminoboku.evernote.EvernoteTestObjectsFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static pl.kiminoboku.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TodoistRequestCreatorTest {
    private static final int ANY_NUMBER = 123;

    @InjectMocks
    TodoistRequestCreator systemUnderTest;

    @Mock
    EvernoteService evernoteServiceMock;
    @Mock
    NoteCloneConditions noteCloneConditionsMock;
    @Mock
    TodoistProjectMappings todoistProjectMappingsMock;
    @Mock
    EvernoteToTodoistRequestConverter evernoteToTodoistRequestConverterMock;

    @Test
    void shouldCreateTaskRequest() {
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification();
        UUID noteGuid = notification.getNoteGuid().get();
        EvernoteNote evernoteNote = EvernoteTestObjectsFactory.createNote();
        TodoistProjectId expectedMappedProjectId = TodoistProjectId.of(ANY_NUMBER);
        TodoistCreateTaskRequest expectedCreateTaskRequest = TodoistTestObjectsFactory.createNewTaskRequest();
        when(evernoteServiceMock.getNote(noteGuid)).thenReturn(Option.of(evernoteNote));
        when(noteCloneConditionsMock.shouldBeCloned(evernoteNote, notification)).thenReturn(true);
        when(todoistProjectMappingsMock.getTargetProjectIdFor(evernoteNote)).thenReturn(Option.of(expectedMappedProjectId));
        when(evernoteToTodoistRequestConverterMock.convert(evernoteNote, expectedMappedProjectId)).thenReturn(expectedCreateTaskRequest);

        Option<TodoistCreateTaskRequest> result = systemUnderTest.requestFor(notification);

        verify(noteCloneConditionsMock).shouldBeCloned(evernoteNote, notification);
        assertThat(result).isDefined();
        assertThat(result.get()).isEqualTo(expectedCreateTaskRequest);
    }

    @Test
    void shouldReturnNoneWhenNoteGuidIsMissing() {
        EvernoteNotification notification = mock(EvernoteNotification.class);
        when(notification.getNoteGuid()).thenReturn(Option.none());

        Option<TodoistCreateTaskRequest> result = systemUnderTest.requestFor(notification);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNoneWhenNoteNotFound() {
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification();
        when(evernoteServiceMock.getNote(any(UUID.class))).thenReturn(Option.none());

        Option<TodoistCreateTaskRequest> result = systemUnderTest.requestFor(notification);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNoneWhenNoteShoulNotBeCloned() {
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification();
        EvernoteNote note = EvernoteTestObjectsFactory.createNote();
        when(evernoteServiceMock.getNote(any(UUID.class))).thenReturn(Option.of(note));
        when(noteCloneConditionsMock.shouldBeCloned(note, notification)).thenReturn(false);

        Option<TodoistCreateTaskRequest> result = systemUnderTest.requestFor(notification);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNoneWhenProjectMappingIsMissing() {
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification();
        EvernoteNote note = EvernoteTestObjectsFactory.createNote();
        when(evernoteServiceMock.getNote(any(UUID.class))).thenReturn(Option.of(note));
        when(noteCloneConditionsMock.shouldBeCloned(note, notification)).thenReturn(true);
        when(todoistProjectMappingsMock.getTargetProjectIdFor(note)).thenReturn(Option.none());

        Option<TodoistCreateTaskRequest> result = systemUnderTest.requestFor(notification);

        assertThat(result).isEmpty();
    }
}