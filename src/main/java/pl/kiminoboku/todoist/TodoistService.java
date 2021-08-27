package pl.kiminoboku.todoist;

import io.vavr.API;
import io.vavr.control.Option;

public class TodoistService {
    public Option<TodoistNewTaskResult> createTask(TodoistCreateTaskRequest createTaskRequest) {
        return API.TODO();
    }
}
