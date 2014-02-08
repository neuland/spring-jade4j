package de.neuland.jade4j.spring.view;

import de.neuland.jade4j.JadeConfiguration;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JadeViewResolver extends AbstractTemplateViewResolver {

	private JadeConfiguration configuration;
	private boolean renderExceptions = false;
	private String contentType = "text/html;charset=UTF-8";

	public JadeViewResolver() {
		setViewClass(requiredViewClass());
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Class requiredViewClass() {
		return JadeView.class;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		JadeView view = (JadeView) super.buildView(viewName);
		view.setConfiguration(this.configuration);
		view.setContentType(contentType);
		view.setRenderExceptions(renderExceptions);
		return view;
	}

	public void setConfiguration(JadeConfiguration configuration) {
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
