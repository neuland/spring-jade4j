[![Build Status](https://secure.travis-ci.org/neuland/spring-jade4j.png?branch=master)](http://travis-ci.org/neuland/spring-jade4j)

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


## Usage

### via Maven

We are using Github for Maven hosting. Just add this repository ...

```xml
<repositories>
  <repository>
    <id>spring-jade4j-releases</id>
    <url>https://raw.github.com/neuland/spring-jade4j/master/releases</url>
  </repository>
</repositories>
```

... and dependency definitions to your `pom.xml`.

```xml
<dependency>
  <groupId>de.neuland</groupId>
  <artifactId>spring-jade4j</artifactId>
  <version>0.2.2</version>
</dependency>
```

## Author

- Stefan Kuper / [planetk](https://github.com/planetk)
- Michael Geers / [naltatis](https://github.com/naltatis)

## License

The MIT License

Copyright (C) 2012 [neuland Büro für Informatik](http://www.neuland-bfi.de/), Bremen, Germany

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.