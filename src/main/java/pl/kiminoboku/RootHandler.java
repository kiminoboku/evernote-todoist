package pl.kiminoboku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
import pl.kiminoboku.spring.LambdaContextConfiguration;
import pl.kiminoboku.spring.SpringSupport;


public class RootHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

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
