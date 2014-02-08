package de.neuland.jade4j.spring.view;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.spring.helper.SpringHelper;
import de.neuland.jade4j.template.JadeTemplate;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

public class JadeView extends AbstractTemplateView {

	private String encoding;
	private JadeConfiguration configuration;
	private boolean renderExceptions = false;
	private String contentType;

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		exposeHelpers(model, request);
		doRender(model, response);
	}

	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		RequestContext requestContext = (RequestContext) model.get(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE);

		if(requestContext != null) {
			model.put(SpringHelper.SPRING_HELPER_NAME, new SpringHelper(requestContext, model));
		}
	}

	protected void doRender(Map<String, Object> model, HttpServletResponse response) throws IOException {
		logger.trace("Rendering Jade template [" + getUrl() + "] in JadeView '" + getBeanName() + "'");

		if (contentType != null) {
			response.setContentType(contentType);
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
