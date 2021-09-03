package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kiminoboku.evernote.EvernoteNotification;
import pl.kiminoboku.evernote.EvernoteNotificationExtractor;
import pl.kiminoboku.evernote.EvernoteService;
import pl.kiminoboku.todoist.TodoistCreateTaskRequest;
import pl.kiminoboku.todoist.TodoistNewTaskResult;
import pl.kiminoboku.todoist.TodoistRequestCreator;
import pl.kiminoboku.todoist.TodoistService;

@Component
@AllArgsConstructor
public class EvernoteNotificationHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static final int HTTP_OK_200 = 200;

    private final EvernoteNotificationExtractor notificationExtractor;
    private final TodoistRequestCreator todoistRequestCreator;
    private final TodoistService todoistService;
    private final EvernoteService evernoteService;
    private final Logger logger;

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        Try.run(() -> {
            Option<EvernoteNotification> notificationOption = createNotificationOptionFrom(input);
            processIfDefined(notificationOption);
        }).onFailure(this::logFailure);

        return okResponse();
    }

    private Option<EvernoteNotification> createNotificationOptionFrom(APIGatewayV2HTTPEvent input) {
        Option<EvernoteNotification> result = notificationExtractor.getFrom(input);
        result.forEach(n -> logger.log("EvernoteNotification: {0}", n));
        return result;
    }

    private void processIfDefined(Option<EvernoteNotification> notificationOption) {
        if (notificationOption.isDefined()) {
            EvernoteNotification notification = notificationOption.get();
            createTaskAndMarkNoteClonedFor(notification);
        }
    }

    private void logFailure(Throwable throwable) {
        logger.log(throwable);
    }

    private APIGatewayV2HTTPResponse okResponse() {
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(HTTP_OK_200)
                .build();
    }

    private void createTaskAndMarkNoteClonedFor(EvernoteNotification notification) {
        Option<TodoistCreateTaskRequest> createTaskRequestOption = createNewTaskRequestFor(notification);
        if (createTaskRequestOption.isDefined()) {
            Option<TodoistNewTaskResult> newTaskResultOption = createTask(createTaskRequestOption.get());
            markNoteClonedIfNewTaskDefined(notification, newTaskResultOption);
        }
    }

    private Option<TodoistCreateTaskRequest> createNewTaskRequestFor(EvernoteNotification notification) {
        return todoistRequestCreator.requestFor(notification);
    }

    private Option<TodoistNewTaskResult> createTask(TodoistCreateTaskRequest createTaskRequest) {
        return todoistService.createTask(createTaskRequest);
    }

    private void markNoteClonedIfNewTaskDefined(EvernoteNotification evernoteNotification, Option<TodoistNewTaskResult> newTaskResultOption) {
        if (newTaskResultOption.isDefined()) {
            evernoteService.markNoteCloned(evernoteNotification.getNoteGuid().get(), newTaskResultOption.get());
        }
    }
}
