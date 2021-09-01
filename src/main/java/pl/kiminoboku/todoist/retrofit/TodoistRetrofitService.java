package pl.kiminoboku.todoist.retrofit;

import pl.kiminoboku.todoist.TodoistCreateTaskRequest;
import pl.kiminoboku.todoist.TodoistNewTaskResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TodoistRetrofitService {
    @POST("tasks")
    Call<TodoistNewTaskResult> createTask(@Body TodoistCreateTaskRequest request);
}
