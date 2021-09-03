package pl.kiminoboku.todoist;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.evernote.EvernoteNote;
import pl.kiminoboku.evernote.EvernoteUserDetails;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EvernoteToTodoistRequestConverter {

    EvernoteUserDetails evernoteUserDetails;

    public TodoistCreateTaskRequest convert(EvernoteNote evernoteNote, TodoistProjectId targetProjectId) {
        UUID noteGuid = evernoteNote.getNoteGuid();
        return TodoistCreateTaskRequest.builder()
                .projectId(targetProjectId.getProjectId())
                .title(evernoteNote.getTitle().getOrElse(noteGuid.toString()))
                .description("Evernote: [Browser](" + webUrl(noteGuid) + ") | [App](" + appUrl(noteGuid) + ")")
                .build();
    }

    private String appUrl(UUID noteGuid) {
        return "evernote:///view/" + evernoteUserDetails.getUserId() + "/" + evernoteUserDetails.getShardId() + "/" + noteGuid + "/" + noteGuid;
    }

    private String webUrl(UUID noteGuid) {
        return evernoteUserDetails.getWebApiUrlPrefix() + "/nl/" + evernoteUserDetails.getUserId() + "/" + noteGuid;
    }
}
