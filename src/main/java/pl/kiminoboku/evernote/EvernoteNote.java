package pl.kiminoboku.evernote;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class EvernoteNote {
    @NonNull
    UUID noteGuid;

    @NonNull
    UUID notebookGuid;

    @NonNull
    String appUrl;

    @NonNull
    String webUrl;

    @Singular
    List<String> tagNames;

    String title;

    public Option<String> getTitle() {
        return Option.of(title);
    }
}
