package de.neuland.jade4j.spring.helper;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.template.JadeTemplate;
import de.neuland.jade4j.template.TemplateLoader;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringHelperTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private RequestContext requestContext;

	@Mock
	private TemplateLoader templateLoader;

	private Map<String, Object> model;

	JadeConfiguration jadeConfig;

	private SpringHelper springHelper;

	@Before
	public void setUp() throws IOException {
		model = new HashMap<String, Object>();
		model.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, requestContext);

		springHelper = new SpringHelper(requestContext, model);

		Map<String, Object> sharedVariables = new HashMap<String, Object>();
		sharedVariables.put(SpringHelper.SPRING_HELPER_NAME, springHelper);

		jadeConfig = new JadeConfiguration();
		jadeConfig.setSharedVariables(sharedVariables);
		jadeConfig.setCaching(false);

		setUpStringTemplateLoader();

		jadeConfig.setTemplateLoader(templateLoader);
	}

	private void setUpStringTemplateLoader() throws IOException {
		when(templateLoader.getLastModified(any(String.class))).thenReturn(-1L);
		when(templateLoader.getReader(any(String.class))).thenAnswer(new Answer<Reader>() {

			@Override
			public Reader answer(InvocationOnMock invocation) throws Throwable {
				String name = (String) invocation.getArguments()[0];
				return new StringReader(name);
			}
		});
	}

	@Test
	public void constructor_no_requestContext() {
		exception.expect(IllegalArgumentException.class);

		new SpringHelper(null, new HashMap<String, Object>());
	}

	@Test
	public void constructure_no_model() {
		exception.expect(IllegalArgumentException.class);

		new SpringHelper(requestContext, null);
	}

	@Test
	public void constructor_with_requestContext() {
		assertThat(springHelper.getRequestContext(), sameInstance(requestContext));
	}

	private String r(String template) {
		try {
			JadeTemplate jadeTemplate = jadeConfig.getTemplate(template);
			return jadeConfig.renderTemplate(jadeTemplate, model);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Test
	public void message_normal_text() {
		String code = "test.code";
		String message = "Test normal message";

		when(requestContext.getMessage(code)).thenReturn(message);
		assertThat(r("p= spring.message('test.code')"), is("<p>" + message + "</p>"));
	}

	@Test
	public void message_escape() {
		String code = "test.code";
		String message = "Test >.< escape message";

		when(requestContext.getMessage(code)).thenReturn(message);
		assertThat(r("p= spring.message('test.code')"), is("<p>" + StringEscapeUtils.escapeHtml4(message) + "</p>"));
	}

	@Test
	public void message_not_escape() {
		String code = "test.code";
		String message = "Test >.< not escape Message";

		when(requestContext.getMessage(code)).thenReturn(message);
		assertThat(r("p!= spring.message('test.code')"), is("<p>" + message + "</p>"));
	}

	@Test
	public void message_middle_of_text_escape() {
		String code = "test.code";
		String message = "Jade >.< Escape";

		when(requestContext.getMessage(code)).thenReturn(message);
		assertThat(r("p Hello, #{spring.message('test.code')}!"), is("<p>Hello, " + StringEscapeUtils.escapeHtml4(message) + "!</p>"));
	}

	@Test
	public void message_middle_of_text_not_escape() {
		String code = "test.code";
		String message = "Jade >.< not escape ";

		when(requestContext.getMessage(code)).thenReturn(message);
		assertThat(r("p Hello, !{spring.message('test.code')}!"), is("<p>Hello, " + message + "!</p>"));
	}

	@Test
	public void messageText() {
		String code = "test.default";
		String defaultMessage = "Hello MessageText";

		when(requestContext.getMessage(code, defaultMessage)).thenReturn(defaultMessage);
		assertThat(r("p= spring.messageText('test.default', '" + defaultMessage + "')"), is("<p>" + defaultMessage + "</p>"));
	}

	@Test
	public void messageTextWithArgs() {
		String code = "test.text.varargs";
		String defaultMessage = "Hell Message Text With Args";

		Double arg1 = 100.11;
		String arg2 = "Arg2";
		String arg3 = "Arg3";

		List<Object> args = new ArrayList<Object>();
		args.add(arg1);
		args.add(arg2);
		args.add(arg3);

		model.put("defaultMessage", defaultMessage);
		model.put("arg1", arg1);
		model.put("arg2", arg2);
		model.put("arg3", arg3);

		when(requestContext.getMessage(code, args, defaultMessage)).thenReturn(defaultMessage);
		assertThat(r("p= spring.messageTextWithArgs('test.text.varargs', defaultMessage, arg1, arg2, arg3)"), is("<p>" + defaultMessage + "</p>"));
	}

	@Test
	public void messageWithArgs() {
		String code = "test.varargs";
		String message = "Hello Message With Args";
		Integer arg1 = 1;
		String arg2 = "arg2";

		List<Object> args = new ArrayList<Object>();
		args.add(arg1);
		args.add(arg2);

		model.put("arg1", arg1);
		model.put("arg2", arg2);

		when(requestContext.getMessage(code, args)).thenReturn(message);
		assertThat(r("p= spring.messageWithArgs('test.varargs', arg1, arg2)"), is("<p>" + message + "</p>"));
	}
}
