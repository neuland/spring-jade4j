package de.neuland.jade4j.spring.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.template.JadeTemplate;

public class JadeView extends AbstractUrlBasedView {

	private String encoding;
	private JadeConfiguration configuration;
	private boolean renderExceptions = false;
	private String contentType;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.trace("Rendering Jade template [" + getUrl() + "] in JadeView '" + getBeanName() + "'");

		if (contentType != null) {
			response.setContentType(contentType);
		}

		/* If we're handling a form with a @ModelAttribute set, we need to unpack BindingResults into a form usable by Jade templates. */
		for (Map.Entry<String, Object> item : model.entrySet()) {

			if (item.getValue() instanceof BeanPropertyBindingResult) {

				BeanPropertyBindingResult bindingResult = (BeanPropertyBindingResult) item.getValue();

				List<String> globalErrors = new ArrayList<String>();

				for (ObjectError globalError : bindingResult.getGlobalErrors()) {
					globalErrors.add(globalError.getDefaultMessage());
				}

				Map<String, String> fieldErrors = new HashMap<String, String>();

				for (FieldError fieldError : bindingResult.getFieldErrors()) {
					fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
				}

				/* Always set these values to avoid Jade 'undefined' errors when checking the values in templates. */
				model.put(bindingResult.getObjectName() + "GlobalErrors", globalErrors);
				model.put(bindingResult.getObjectName() + "FieldErrors", fieldErrors);
				model.put(bindingResult.getObjectName() + "HasErrors", !fieldErrors.isEmpty() || !globalErrors.isEmpty());

				/* Remove the unusable BindingResult from the Jade model. */
				model.remove(BindingResult.MODEL_KEY_PREFIX + bindingResult.getObjectName());
			}
		}

		PrintWriter responseWriter = response.getWriter();

		if (renderExceptions) {
			Writer writer = new StringWriter();
			try {
				configuration.renderTemplate(getTemplate(), model, writer);
				responseWriter.write(writer.toString());
			} catch (JadeException e) {
				String htmlString = e.toHtmlString(writer.toString());
				responseWriter.write(htmlString);
				logger.error("failed to render template [" + getUrl() + "]", e);
			} catch (IOException e) {
				responseWriter.write("<pre>could not find template: " + getUrl() + "\n");
				e.printStackTrace(responseWriter);
				responseWriter.write("</pre>");
				logger.error("could not find template", e);
			}
		} else {
			try {
				configuration.renderTemplate(getTemplate(), model, responseWriter);
			} catch (Throwable e) {
				logger.error("failed to render template [" + getUrl() + "]\n", e);
			}
		}
	}

	protected JadeTemplate getTemplate() throws IOException, JadeException {
		return configuration.getTemplate(getUrl());
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return configuration.templateExists(getUrl());
	}

	protected void processTemplate(JadeTemplate template, Map<String, Object> model, HttpServletResponse response) throws IOException {
		try {
			configuration.renderTemplate(template, model, response.getWriter());
		} catch (JadeCompilerException e) {
			e.printStackTrace();
		}
	}

	/* Configuration Handling */
	public JadeConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JadeConfiguration configuration) {
		this.configuration = configuration;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setRenderExceptions(boolean renderExceptions) {
		this.renderExceptions = renderExceptions;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
