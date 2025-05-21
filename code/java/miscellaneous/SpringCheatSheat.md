
# Spring Framework Cheat Sheet

### @Component marks a class as a Spring-managed component for auto-detection and registration in the application context.

```java
import org.springframework.stereotype.Component;

@Component
public class BookService {
    public void listBooks() {
        System.out.println("Listing all books");
    }
}
```

### @Controller is a specialized @Component for Spring MVC controllers that handle web requests.

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {
    @GetMapping("/books")
    public String showBooks() {
        return "books"; // Returns view name "books"    
    }
}
```

### @Autowired enables automatic dependency injection in Spring-managed beans.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    public void displayBooks() {
        bookService.listBooks();
    }
}
```


### @Configuration defines a configuration class that declares beans and configurations for the Spring container.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public BookService bookService() {
        return new BookService();
    }
}
```

### @RequestMapping maps web requests to handler methods in Spring MVC applications.

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {
    @RequestMapping("/books")
    public String getBooks() {
        return "books";
    }
}
```

or 

```java
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/books")
public class BookController {
    @GetMapping
    public String getBooks() {
        return "books";
    }
}
```


### @PathVariable extracts values from the URL and binds them to method parameters.

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {
    @GetMapping("/books/{id}")
    public String getBookById(@PathVariable("id") String bookId) {
        System.out.println("Book ID: " + bookId);
        return "bookDetails";
    }
}
```

### @RestController is a combination of @Controller and @ResponseBody, used for building RESTful web services.

```java
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@RestController
public class BookRestController {
    @GetMapping("/api/books")
    public List<String> getAllBooks() {
        return Arrays.asList("Spring Boot", "Spring Cloud");
    }
}
```

### @RequestParam extracts query parameters from the URL and binds them to method parameters.

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookRestController {
    @GetMapping("/api/book")
    public String getBookByTitle(@RequestParam("title") String title) {
        return "Book title: " + title;
    }
}
```

### @ResponseBody indicates that a method's return value should be written directly to the HTTP response body.

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookRestController {
    @GetMapping("/api/message")
    @ResponseBody
    public String getMessage() {
        return "Hello, Spring!";
    }
}
```

### @Value injects values from properties files or environment variables into Spring beans.

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Library {
    @Value("${library.name}")
    private String libraryName;

    // This will inject the value of the database.url environment variable 
    // into the databaseUrl variable. With Default Value
    @Value("${database.url}")
    private String databaseUrl;

    // This will inject the value of the log.level environment variable. 
    // If the environment variable is not found, it will default to "INFO". Using SpEL. 
    @Value("${log.level:INFO}")
    private String logLevel;

    //This will inject the value of the MY_VARIABLE environment variable. 
    // If it's not found, it will use "default_value".
    @Value("#{systemEnvironment['MY_VARIABLE'] ?: 'default_value'}")
    private String myVariable;
    
    public void printLibraryName() {
        System.out.println("Library Name: " + libraryName);
    }
}
```

### @Scope defines the scope of a bean, such as singleton or prototype.

```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Book {
    // Prototype-scoped bean
}
```
