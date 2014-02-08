package de.neuland.jade4j.spring.helper;

import org.springframework.util.Assert;
import org.springframework.web.servlet.support.RequestContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpringHelper {
	public static final String SPRING_HELPER_NAME = "spring";

	private Map<String, Object> model;

	private RequestContext requestContext;

	public SpringHelper(RequestContext requestContext, Map<String, Object> model) {
		Assert.notNull(requestContext);
		Assert.notNull(model);

		this.model = model;
		this.requestContext = requestContext;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public RequestContext getRequestContext() {
		return requestContext;
	}

	public String message(String code) {
		return requestContext.getMessage(code);
	}

	public String messageText(String code, String defaultText) {
		return requestContext.getMessage(code, defaultText);
	}

	public String messageWithArgs(String code, Object... args) {
		List<Object> messageArgs = Arrays.asList(args);
		return requestContext.getMessage(code, messageArgs);
	}

	public String messageTextWithArgs(String code, String defaultText, Object... args) {
		List<Object> messageArgs = Arrays.asList(args);
		return requestContext.getMessage(code, messageArgs, defaultText);
	}

}
