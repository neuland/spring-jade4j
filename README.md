[![Build Status](https://secure.travis-ci.org/neuland/spring-jade4j.png?branch=master)](http://travis-ci.org/neuland/spring-jade4j)

# Attention: spring-jade4j is now spring-pug4j
In alignment with the javascript template engine we renamed spring-jade4j to spring-pug4j. You will find it under https://github.com/neuland/spring-pug4j

Please report spring-pug4j issues in the new repository.

# A Spring Integration for Jade4J

See [neuland/jade4j](https://github.com/neuland/jade4j) for more information.

## Bean Declarations

applicationContext.xml

```xml
<bean id="templateLoader" class="de.neuland.jade4j.spring.template.SpringTemplateLoader">
	<property name="basePath" value="/WEB-INF/views/" />
</bean>

<bean id="jadeConfiguration" class="de.neuland.jade4j.JadeConfiguration">
	<property name="prettyPrint" value="false" />
	<property name="caching" value="false" />
	<property name="templateLoader" ref="templateLoader" />
</bean>

<bean id="viewResolver" class="de.neuland.jade4j.spring.view.JadeViewResolver">
	<property name="configuration" ref="jadeConfiguration" />
	<!-- rendering nice html formatted error pages for development -->
	<property name="renderExceptions" value="true" />
</bean>
```
Or, if you are using Spring JavaConfig:

```java
@Configuration
public class JadeConfig {

	@Bean
	public SpringTemplateLoader templateLoader() {
		SpringTemplateLoader templateLoader = new SpringTemplateLoader();
		templateLoader.setBasePath("/WEB-INF/views/");
		templateLoader.setEncoding("UTF-8");
		templateLoader.setSuffix(".jade");
		return templateLoader;
	}

	@Bean
	public JadeConfiguration jadeConfiguration() {
		JadeConfiguration configuration = new JadeConfiguration();
		configuration.setCaching(false);
		configuration.setTemplateLoader(templateLoader());
		return configuration;
	}

	@Bean
	public ViewResolver viewResolver() {
		JadeViewResolver viewResolver = new JadeViewResolver();
		viewResolver.setConfiguration(jadeConfiguration());
		return viewResolver;
	}
}
```

## Usage

### via Maven

As of release 0.4.0 we changed maven hosting to sonatype. using Github Maven Repository is no longer required

Please be aware that we had to change the group id from 'de.neuland' to 'de.neuland-bfi' in order to meet sonatype conventions for group naming.

Just add following dependency definitions to your `pom.xml`.

```xml
<dependency>
  <groupId>de.neuland-bfi</groupId>
  <artifactId>spring-jade4j</artifactId>
  <version>1.3.1</version>
</dependency>
```

## Author

- Stefan Kuper / [planetk](https://github.com/planetk)
- Michael Geers / [naltatis](https://github.com/naltatis)

## License

The MIT License

Copyright (C) 2012-2019 [neuland Büro für Informatik](http://www.neuland-bfi.de/), Bremen, Germany

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
