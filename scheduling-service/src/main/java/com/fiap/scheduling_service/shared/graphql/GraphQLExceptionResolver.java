package com.fiap.scheduling_service.shared.graphql;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof SecurityException) {
            return buildError(ex, env, ErrorType.FORBIDDEN);
        }
        if (ex instanceof NoSuchElementException) {
            return buildError(ex, env, ErrorType.NOT_FOUND);
        }
        if (ex instanceof IllegalArgumentException) {
            return buildError(ex, env, ErrorType.BAD_REQUEST);
        }
        return null;
    }

    private GraphQLError buildError(Throwable ex, DataFetchingEnvironment env, ErrorType errorType) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(errorType)
                .message(ex.getMessage())
                .build();
    }
}
