package de.neuland.jade4j.spring.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import de.neuland.jade4j.template.TemplateLoader;

public class SpringTemplateLoader implements TemplateLoader {

	private final ResourceLoader resourceLoader;

	private String encoding = "UTF-8";
	private String suffix = ".jade";
	private String basePath = "";

	public SpringTemplateLoader() {
		this.resourceLoader = new DefaultResourceLoader();
	}

	@Override
	public long getLastModified(String name) {
		Resource resource = getResource(name);
		try {
			return resource.lastModified();
		} catch (IOException ex) {
			return -1;
		}
	}

	@Override
	public Reader getReader(String name) throws IOException {
		Resource resource = getResource(name);
		return new InputStreamReader(resource.getInputStream(), encoding);
	}

	private Resource getResource(String name) {
		String resourceName = basePath + name;
		if (!resourceName.endsWith(suffix)) {
			resourceName += suffix;
		}
		return this.resourceLoader.getResource(resourceName);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

}
