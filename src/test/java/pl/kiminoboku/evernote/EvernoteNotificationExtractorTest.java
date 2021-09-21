package pl.kiminoboku.evernote;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.kiminoboku.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EvernoteNotificationExtractorTest {
    @InjectMocks
    EvernoteNotificationExtractor systemUnderTest;

    @Mock
    APIGatewayV2HTTPEvent inputMock;

    @Test
    void shouldReturnNoneWhenQueryStringParametersAreMissing() {
        when(inputMock.getQueryStringParameters()).thenReturn(null);

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNotification() {
        String userId = "ANY_USER_ID";
        String noteGuid = "6420a4a7-a5d6-4630-b09d-bf8c392a5850";
        String notebookGuid = "8f232448-1223-42fb-9448-096b39b51746";
        String reason = "create";
        Map<String, String> queryParameters = HashMap.of(
                        "userId", userId,
                        "guid", noteGuid,
                        "notebookGuid", notebookGuid,
                        "reason", reason)
                .toJavaMap();
        when(inputMock.getQueryStringParameters()).thenReturn(queryParameters);
        EvernoteNotification expectedNotification = EvernoteNotification.builder()
                .userId(userId)
                .noteGuid(UUID.fromString(noteGuid))
                .notebookGuid(UUID.fromString(notebookGuid))
                .notificationReason(NotificationReason.NOTE_CREATED)
                .build();

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isDefined();
        EvernoteNotification evernoteNotification = result.get();
        assertThat(evernoteNotification).isEqualTo(expectedNotification);
    }

    @Test
    void shouldReturnNotificationWhenNoteGuidIsMissing() {
        String userId = "ANY_USER_ID";
        String notebookGuid = "8f232448-1223-42fb-9448-096b39b51746";
        String reason = "create";
        Map<String, String> queryParameters = HashMap.of(
                        "userId", userId,
                        "notebookGuid", notebookGuid,
                        "reason", reason)
                .toJavaMap();
        when(inputMock.getQueryStringParameters()).thenReturn(queryParameters);
        EvernoteNotification expectedNotification = EvernoteNotification.builder()
                .userId(userId)
                .notebookGuid(UUID.fromString(notebookGuid))
                .notificationReason(NotificationReason.NOTE_CREATED)
                .build();

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isDefined();
        EvernoteNotification evernoteNotification = result.get();
        assertThat(evernoteNotification).isEqualTo(expectedNotification);
    }

    @Test
    void shouldReturnNoneWhenUserIdIsMissing() {
        String noteGuid = "6420a4a7-a5d6-4630-b09d-bf8c392a5850";
        String notebookGuid = "8f232448-1223-42fb-9448-096b39b51746";
        String reason = "create";
        Map<String, String> queryParameters = HashMap.of(
                        "guid", noteGuid,
                        "notebookGuid", notebookGuid,
                        "reason", reason)
                .toJavaMap();
        when(inputMock.getQueryStringParameters()).thenReturn(queryParameters);

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNoneWhenNotebookGuidIsMissing() {
        String userId = "ANY_USER_ID";
        String noteGuid = "6420a4a7-a5d6-4630-b09d-bf8c392a5850";
        String reason = "create";
        Map<String, String> queryParameters = HashMap.of(
                        "userId", userId,
                        "guid", noteGuid,
                        "reason", reason)
                .toJavaMap();
        when(inputMock.getQueryStringParameters()).thenReturn(queryParameters);

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNoneWhenReasonIsMissing() {
        String userId = "ANY_USER_ID";
        String noteGuid = "6420a4a7-a5d6-4630-b09d-bf8c392a5850";
        String notebookGuid = "8f232448-1223-42fb-9448-096b39b51746";
        Map<String, String> queryParameters = HashMap.of(
                        "userId", userId,
                        "guid", noteGuid,
                        "notebookGuid", notebookGuid)
                .toJavaMap();
        when(inputMock.getQueryStringParameters()).thenReturn(queryParameters);

        Option<EvernoteNotification> result = systemUnderTest.getFrom(inputMock);

        assertThat(result).isEmpty();
    }
}