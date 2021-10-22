package org.easybank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import org.easybank.aggregate.AggregateIdBase;
import org.easybank.cqrs.Response;
import org.easybank.cqrs.command.CommandBase;
import org.easybank.cqrs.query.Query;
import org.easybank.writelane.EventStoreServiceImpl;
import org.jooq.DSLContext;

import java.util.function.Function;

/**
 * @author Nikolay Smirnov
 */
public abstract class ControllerConfiguration {
    final EventStoreServiceImpl eventStoreService;
    final DSLContext dslContext;

    private final Javalin app;
    private final ObjectMapper objectMapper;

    public ControllerConfiguration(Javalin app, EventStoreServiceImpl eventStoreService, DSLContext dslContext) {
        this.eventStoreService = eventStoreService;
        this.dslContext = dslContext;
        this.app = app;
        this.objectMapper = JavalinJackson.getObjectMapper();
    }

    public abstract void init();

    <ID extends AggregateIdBase<ID>, CMD extends CommandBase<ID>>
    void initPostRequest(String url, Function<Context, CMD> aggregateIdToCommandFunc, Function<CMD, Response> responseFunc) {
        app.post(url, buildModifyRequest(aggregateIdToCommandFunc, responseFunc));
    }

    <ID extends AggregateIdBase<ID>, CMD extends CommandBase<ID>>
    void initPutRequest(String url, Function<Context, CMD> aggregateIdToCommandFunc, Function<CMD, Response> responseFunc) {
        app.put(url, buildModifyRequest(aggregateIdToCommandFunc, responseFunc));
    }

    <QUERY extends Query>
    void initGetRequest(String url, Function<Context, QUERY> aggregateIdToQueryFunc, Function<QUERY, Response> responseFunc) {
        app.get(url,
                ctx -> {
                    QUERY query = aggregateIdToQueryFunc.apply(ctx);
                    ctx.json(responseFunc.apply(query));
                }
        );
    }

    private <ID extends AggregateIdBase<ID>, CMD extends CommandBase<ID>>
    Handler buildModifyRequest(Function<Context, CMD> aggregateIdToCommandFunc, Function<CMD, Response> responseFunc) {
        return ctx -> {
            CMD command = aggregateIdToCommandFunc.apply(ctx);
            var reader = objectMapper.readerForUpdating(command);
            ctx.json(responseFunc.apply(reader.<CMD>readValue(ctx.body())));
        };
    }
}
