package de.neuland.jade4j.spring.view;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.helper.SpringHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JadeViewTest {

	private JadeView jadeView;

	@Mock
	private JadeConfiguration configuration;

	@Mock
	private HttpServletRequest request;

	@Mock
	private RequestContext requestContext;

	@Before
	public void setUp() {
		jadeView = new JadeView();
	}

	@Test
	public void exposeHelpers_expose() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, requestContext);

		jadeView.exposeHelpers(model, request);

		SpringHelper springHelper = (SpringHelper) model.get(SpringHelper.SPRING_HELPER_NAME);
		assertThat(springHelper, not(nullValue()));
		assertThat(springHelper.getRequestContext(), is(requestContext));
	}

	@Test
	public void exposeHelpers_not_expose() throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		jadeView.exposeHelpers(model, request);

		SpringHelper springHelper = (SpringHelper) model.get(SpringHelper.SPRING_HELPER_NAME);
		assertThat(springHelper, nullValue());
	}
}
