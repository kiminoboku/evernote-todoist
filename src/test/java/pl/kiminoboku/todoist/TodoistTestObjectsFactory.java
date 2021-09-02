package pl.kiminoboku.todoist;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoistTestObjectsFactory {

    public static final int PROJECT_ID = 123;
    public static final String TASK_TITLE = "ANY_TASK_TITLE";
    public static final String TASK_DESCRIPTION = "ANY_TASK_DESCRIPTION";
    public static final int TASK_ID = 456;
    public static final String TASK_URL = "ANY_TASK_URL";

    public static TodoistCreateTaskRequest createNewTaskRequest() {
        return TodoistCreateTaskRequest.builder()
                .projectId(PROJECT_ID)
                .title(TASK_TITLE)
                .description(TASK_DESCRIPTION)
                .build();
    }

    public static TodoistNewTaskResult createNewTaskResult() {
        return TodoistNewTaskResult.builder()
                .id(TASK_ID)
                .url(TASK_URL)
                .build();
    }
}
