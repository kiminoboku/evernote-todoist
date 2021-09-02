package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteNotificationExtractor;
import pl.kiminoboku.evernote.EvernoteService;
import pl.kiminoboku.evernote.EvernoteTestObjectsFactory;
import pl.kiminoboku.todoist.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvernoteNotificationHandlerTest {
    private static final int HTTP_OK_200 = 200;

    @InjectMocks
    EvernoteNotificationHandler systemUnderTest;

    @Mock
    EvernoteNotificationExtractor evernoteNotificationExtractorMock;
    @Mock
    TodoistRequestCreator todoistRequestCreatorMock;
    @Mock
    TodoistService todoistServiceMock;
    @Mock
    EvernoteService evernoteServiceMock;
    @Mock
    Logger loggerMock;

    @Mock
    APIGatewayV2HTTPEvent inputMock;
    @Mock
    Context lambdaContextMock;

    @Test
    void shouldLogErrorsAndAlwaysReturnCodeOk() {
        when(evernoteNotificationExtractorMock.getFrom(any())).thenThrow(IllegalArgumentException.class);

        APIGatewayV2HTTPResponse result = systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(loggerMock).log(any(Throwable.class));
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HTTP_OK_200);
    }

    @Test
    void shouldNotProceedIfNotificationDataIsMissing() {
        when(evernoteNotificationExtractorMock.getFrom(any())).thenReturn(Option.none());

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verifyNoInteractions(todoistRequestCreatorMock);
    }

    @Test
    void shouldNotProceedWhenTodoistRequestIsMissing() {
        EvernoteNotification evernoteNotificationMock = mock(EvernoteNotification.class);
        when(evernoteNotificationExtractorMock.getFrom(inputMock)).thenReturn(Option.of(evernoteNotificationMock));
        when(todoistRequestCreatorMock.requestFor(evernoteNotificationMock)).thenReturn(Option.none());

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verifyNoInteractions(todoistServiceMock);
    }

    @Test
    void shouldNotProceedWhenTodoistTaskIsNotCreated() {
        EvernoteNotification evernoteNotificationMock = mock(EvernoteNotification.class);
        when(evernoteNotificationExtractorMock.getFrom(inputMock)).thenReturn(Option.of(evernoteNotificationMock));
        TodoistCreateTaskRequest createTaskRequestMock = mock(TodoistCreateTaskRequest.class);
        when(todoistRequestCreatorMock.requestFor(evernoteNotificationMock)).thenReturn(Option.of(createTaskRequestMock));
        when(todoistServiceMock.createTask(createTaskRequestMock)).thenReturn(Option.none());

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verifyNoInteractions(evernoteServiceMock);
    }

    @Test
    void shouldRetrieveEvernoteInput() {
        when(evernoteNotificationExtractorMock.getFrom(any())).thenReturn(Option.none());

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(evernoteNotificationExtractorMock).getFrom(inputMock);
    }

    @Test
    void shouldProcessRequest() {
        EvernoteNotification notification = EvernoteTestObjectsFactory.createNotification();
        TodoistCreateTaskRequest createTaskRequest = TodoistTestObjectsFactory.createNewTaskRequest();
        TodoistNewTaskResult createTaskResult = TodoistTestObjectsFactory.createNewTaskResult();
        UUID noteGuid = EvernoteTestObjectsFactory.NOTE_GUID;
        when(evernoteNotificationExtractorMock.getFrom(inputMock)).thenReturn(Option.of(notification));
        when(todoistRequestCreatorMock.requestFor(notification)).thenReturn(Option.of(createTaskRequest));
        when(todoistServiceMock.createTask(createTaskRequest)).thenReturn(Option.of(createTaskResult));
        when(evernoteServiceMock.markNoteCloned(noteGuid, createTaskResult)).thenReturn(true);

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(evernoteServiceMock).markNoteCloned(noteGuid, createTaskResult);
    }
}