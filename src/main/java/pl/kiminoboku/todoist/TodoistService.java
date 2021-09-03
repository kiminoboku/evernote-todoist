package pl.kiminoboku.todoist;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.Logger;
import pl.kiminoboku.todoist.retrofit.TodoistRetrofitService;
import retrofit2.Call;
import retrofit2.Response;

@Component
@AllArgsConstructor
public class TodoistService {

    TodoistRetrofitService todoistRetrofitService;
    Logger logger;

    public Option<TodoistNewTaskResult> createTask(TodoistCreateTaskRequest createTaskRequest) {
        return Try.of(() -> todoistRetrofitService.createTask(createTaskRequest))
                .mapTry(Call::execute)
                .andThen(response -> logger.log("Todoist rest response: {0}", response.code()))
                .map(Response::body)
                .onFailure(logger::log)
                .toOption();
    }
}
