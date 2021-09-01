package pl.kiminoboku.todoist;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TodoistCreateTaskRequest {
    @NonNull
    @SerializedName("content")
    String title;

    @NonNull
    String description;

    @SerializedName("project_id")
    int projectId;
}
