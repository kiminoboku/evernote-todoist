package pl.kiminoboku.evernote;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class EvernoteUserDetails {
    @NonNull
    String userId;

    @NonNull
    String shardId;

    @NonNull
    String webApiUrlPrefix;
}
