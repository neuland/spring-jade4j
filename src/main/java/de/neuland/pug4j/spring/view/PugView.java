package de.neuland.pug4j.spring.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.neuland.pug4j.PugConfiguration;
import de.neuland.pug4j.exceptions.PugCompilerException;
import de.neuland.pug4j.exceptions.PugException;
import de.neuland.pug4j.template.PugTemplate;
import org.springframework.web.servlet.view.AbstractTemplateView;


public class PugView extends AbstractTemplateView {

	private String encoding;
	private PugConfiguration configuration;
	private boolean renderExceptions = false;
	private String contentType;

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		doRender(model, response);
	}

	private void doRender(Map<String, Object> model, HttpServletResponse response) throws IOException {
		logger.trace("Rendering Pug template [" + getUrl() + "] in PugView '" + getBeanName() + "'");

		if (contentType != null) {
			response.setContentType(contentType);
		}

		PrintWriter responseWriter = response.getWriter();

		if (renderExceptions) {
			Writer writer = new StringWriter();
			try {
				configuration.renderTemplate(getTemplate(), model, writer);
				responseWriter.write(writer.toString());
			} catch (PugException e) {
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

	protected PugTemplate getTemplate() throws IOException, PugException {
		return configuration.getTemplate(getUrl());
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return configuration.templateExists(getUrl());
	}

	protected void processTemplate(PugTemplate template, Map<String, Object> model, HttpServletResponse response) throws IOException {
		try {
			configuration.renderTemplate(template, model, response.getWriter());
		} catch (PugCompilerException e) {
			e.printStackTrace();
		}
	}

	/* Configuration Handling */
	public PugConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PugConfiguration configuration) {
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
