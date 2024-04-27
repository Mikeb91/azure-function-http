package com.sergi.hellofunc.functions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReactiveReadListFunction extends FunctionInvoker<List<String>, String> {

	private static final Log logger = LogFactory.getLog(ReactiveReadListFunction.class);

	@FunctionName("readList")
	public String execute(@HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
			HttpRequestMessage<List<String>> request, ExecutionContext context
	) {
		return handleRequest(request.getBody(), context);
	}

	@Override
	protected String postProcessFluxFunctionResult(List<String> rawInputs, Object functionInputs, Flux<?> functionResult,
		FunctionInvocationWrapper function, ExecutionContext executionContext
	) {
		functionResult
			.doFirst(() -> executionContext.getLogger().info("BEGIN reading list ..."))//ejecutar acciones antes de procesar el elemento
			.mapNotNull((v) -> v.toString().toUpperCase()) //procesa elemento
			.doFinally((signalType) -> executionContext.getLogger().info("END reading list")) //ejecutar acciones después del elemento
			.subscribe((v) -> executionContext.getLogger().info("   " + v));
		return "This is the provided list: " + rawInputs;
	}

}
