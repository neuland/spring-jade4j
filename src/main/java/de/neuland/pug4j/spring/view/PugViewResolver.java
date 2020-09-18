package de.neuland.pug4j.spring.view;

import de.neuland.pug4j.PugConfiguration;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class PugViewResolver extends AbstractTemplateViewResolver {

	private PugConfiguration configuration;
	private boolean renderExceptions = false;
	private String contentType = "text/html;charset=UTF-8";

	public PugViewResolver() {
		setViewClass(requiredViewClass());
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Class requiredViewClass() {
		return PugView.class;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		PugView view = (PugView) super.buildView(viewName);
		view.setConfiguration(this.configuration);
		view.setContentType(contentType);
		view.setRenderExceptions(renderExceptions);
		return view;
	}

	public void setConfiguration(PugConfiguration configuration) {
		this.configuration = configuration;
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
