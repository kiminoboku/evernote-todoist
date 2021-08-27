package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
import pl.kiminoboku.spring.LambdaContextConfiguration;
import pl.kiminoboku.spring.SpringSupport;

import java.util.UUID;


public class RootHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static final Set<UUID> NOTEBOOK_UUIDS = HashSet.of(
            UUID.fromString("50d41f65-02ec-7f28-d144-c5df7b04ceaa"), //Dom
            UUID.fromString("41bb3166-1846-0439-d1be-21cfcc1745eb"), //Mermet
            UUID.fromString("13e549ce-e958-4854-3094-31fc275e3ae9") //Praca
    );

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        LambdaContextConfiguration.setContext(context);
        ResolvableType handlerType = getHandlerType();
        Class<RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>> handlerClass = getHandlerClass(handlerType);
        RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> handlerBean = getApplicationContext().getBean(handlerClass);
        return handlerBean.handleRequest(input, context);
    }

    private ResolvableType getHandlerType() {
        return ResolvableType.forClassWithGenerics(RequestHandler.class, APIGatewayV2HTTPEvent.class, APIGatewayV2HTTPResponse.class);
    }

    @SuppressWarnings("unchecked")
    private Class<RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>> getHandlerClass(ResolvableType handlerType) {
        return (Class<RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>>) handlerType.resolve();
    }

    private GenericApplicationContext getApplicationContext() {
        return SpringSupport.getApplicationContext();
    }
}
