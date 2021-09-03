package pl.kiminoboku.todoist;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kiminoboku.Logger;
import pl.kiminoboku.todoist.retrofit.TodoistRetrofitService;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoistServiceTest {
    @InjectMocks
    TodoistService systemUnderTest;

    @Mock
    TodoistRetrofitService todoistRetrofitServiceMock;
    @Mock
    Logger loggerMock;

    @Test
    @SuppressWarnings("unchecked")
    void shouldCreateTask() throws IOException {
        TodoistCreateTaskRequest taskRequest = TodoistTestObjectsFactory.createNewTaskRequest();
        Call<TodoistNewTaskResult> call = mock(Call.class);
        TodoistNewTaskResult expectedResult = TodoistTestObjectsFactory.createNewTaskResult();
        when(call.execute()).thenReturn(Response.success(expectedResult));
        when(todoistRetrofitServiceMock.createTask(taskRequest)).thenReturn(call);

        Option<TodoistNewTaskResult> result = systemUnderTest.createTask(taskRequest);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(Option.of(expectedResult));
    }

    @Test
    void shouldLogErrors() {
        TodoistCreateTaskRequest taskRequest = TodoistTestObjectsFactory.createNewTaskRequest();
        when(todoistRetrofitServiceMock.createTask(taskRequest)).thenThrow(IllegalArgumentException.class);

        systemUnderTest.createTask(taskRequest);

        verify(loggerMock).log(any(IllegalArgumentException.class));
    }
}