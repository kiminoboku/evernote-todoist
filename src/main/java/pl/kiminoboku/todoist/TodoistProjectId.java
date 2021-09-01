package pl.kiminoboku.todoist;

import lombok.Value;

@Value
public class TodoistProjectId {
    int projectId;

    public static TodoistProjectId of(int projectId) {
        return new TodoistProjectId(projectId);
    }
}