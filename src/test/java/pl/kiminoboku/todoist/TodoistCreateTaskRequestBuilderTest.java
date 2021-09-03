package pl.kiminoboku.todoist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteTestObjectsFactory;
import pl.kiminoboku.evernote.EvernoteUserDetails;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoistCreateTaskRequestBuilderTest {
    private static final String WEB_API_URL_PREFIX = "ANY_WEB_API_URL_PREFIX";
    private static final String USER_ID = "ANY_USER_ID";
    private static final String SHARD_ID = "ANY_SHARD_ID";

    @InjectMocks
    TodoistCreateTaskRequestBuilder systemUnderTest;

    @Mock
    EvernoteUserDetails evernoteUserDetails;

    @Test
    void shouldBuildRequest() {
        EvernoteNote evernoteNote = EvernoteTestObjectsFactory.createEvernoteNote();
        when(evernoteUserDetails.getWebApiUrlPrefix()).thenReturn(WEB_API_URL_PREFIX);
        when(evernoteUserDetails.getUserId()).thenReturn(USER_ID);
        when(evernoteUserDetails.getShardId()).thenReturn(SHARD_ID);
        int projectId = 123;
        UUID noteGuid = evernoteNote.getNoteGuid();
        String expectedDescription = "Evernote: [Browser](" + WEB_API_URL_PREFIX + "/nl/" + USER_ID + "/" + noteGuid + ") | [App](evernote:///view/" + USER_ID + "/" + SHARD_ID + "/" + noteGuid + "/" + noteGuid + ")";

        TodoistCreateTaskRequest result = systemUnderTest.buildFrom(evernoteNote, TodoistProjectId.of(projectId));

        assertThat(result).isEqualTo(TodoistCreateTaskRequest.builder()
                .projectId(projectId)
                .title(EvernoteTestObjectsFactory.NOTE_TITLE)
                .description(expectedDescription)
                .build());
    }
}