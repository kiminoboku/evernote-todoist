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
import pl.kiminoboku.exception.ThrowableLogger;
import pl.kiminoboku.todoist.TodoistRequestCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvernoteNotificationHandlerTest {
    private static final int HTTP_OK_200 = 200;

    @InjectMocks
    EvernoteNotificationHandler systemUnderTest;

    @Mock
    ThrowableLogger throwableLoggerMock;
    @Mock
    EvernoteNotificationExtractor evernoteNotificationExtractorMock;
    @Mock
    TodoistRequestCreator todoistRequestCreatorMock;
    @Mock
    APIGatewayV2HTTPEvent inputMock;
    @Mock
    Context lambdaContextMock;

    @Test
    void shouldLogErrorsAndAlwaysReturnCodeOk() {
        when(evernoteNotificationExtractorMock.getFrom(any())).thenThrow(IllegalArgumentException.class);

        APIGatewayV2HTTPResponse result = systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(throwableLoggerMock).log(any());
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HTTP_OK_200);
    }

    @Test
    void shouldRetrieveEvernoteInput() {
        when(evernoteNotificationExtractorMock.getFrom(any())).thenReturn(Option.none());

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(evernoteNotificationExtractorMock).getFrom(inputMock);
    }

    @Test
    void shouldCreateTodoistRequestsBasedOnEvernoteInput() {
        EvernoteNotification evernoteNotificationMock = mock(EvernoteNotification.class);
        when(evernoteNotificationExtractorMock.getFrom(inputMock)).thenReturn(Option.of(evernoteNotificationMock));

        systemUnderTest.handleRequest(inputMock, lambdaContextMock);

        verify(todoistRequestCreatorMock).requestFor(evernoteNotificationMock);
    }
}