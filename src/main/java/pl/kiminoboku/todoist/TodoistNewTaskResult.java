package pl.kiminoboku.todoist;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TodoistNewTaskResult {
    int id;

    @NonNull
    String url;
}
