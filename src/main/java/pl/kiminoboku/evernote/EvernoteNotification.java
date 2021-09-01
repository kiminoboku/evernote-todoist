package pl.kiminoboku.evernote;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class EvernoteNotification {
    @NonNull
    String userId;

    @NonNull
    UUID notebookGuid;

    @NonNull
    NotificationReason notificationReason;

    UUID noteGuid;

    public Option<UUID> getNoteGuid() {
        return Option.of(noteGuid);
    }
}
