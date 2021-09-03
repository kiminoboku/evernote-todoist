package pl.kiminoboku.todoist;

import lombok.Value;

@Value(staticConstructor = "of")
public class TodoistProjectId {
    int projectId;
}