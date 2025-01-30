# Audition API

The purpose of this Spring Boot application is to test general knowledge of SpringBoot, Java, Gradle etc. It is created
for hiring needs of our company but can be used for other purposes.

## Overarching expectations & Assessment areas

<pre>
This is not a university test. 
This is meant to be used for job applications and MUST showcase your full skillset. 
<b>As such, PRODUCTION-READY code must be written and submitted. </b> 
</pre>

- clean, easy to understand code
- good code structures
- Proper code encapsulation
- unit tests with minimum 80% coverage.
- A Working application to be submitted.
- Observability. Does the application contain Logging, Tracing and Metrics instrumentation?
- Input validation.
- Proper error handling.
- Ability to use and configure rest template. We allow for half-setup object mapper and rest template
- Not all information in the Application is perfect. It is expected that a person would figure these out and correct.

## Getting Started

### Prerequisite tooling

- Any Springboot/Java IDE. Ideally IntelliJIdea.
- Java 17
- Gradle 8

### Prerequisite knowledge

- Java
- SpringBoot
- Gradle
- Junit

### Importing Google Java codestyle into INtelliJ

```
- Go to IntelliJ Settings
- Search for "Code Style"
- Click on the "Settings" icon next to the Scheme dropdown
- Choose "Import -> IntelliJ Idea code style XML
- Pick the file "google_java_code_style.xml" from root directory of the application
__Optional__
- Search for "Actions on Save"
    - Check "Reformat Code" and "Organise Imports"
```

---
**NOTE** -
It is highly recommended that the application be loaded and started up to avoid any issues.

---

## Audition Application information

This section provides information on the application and what the needs to be completed as part of the audition
application.

The audition consists of multiple TODO statements scattered throughout the codebase. The applicants are expected to:

- Complete all the TODO statements.
- Add unit tests where applicants believe it to be necessary.
- Make sure that all code quality check are completed.
- Gradle build completes sucessfully.
- Make sure the application if functional.

## Submission process

Applicants need to do the following to submit their work:

- Clone this repository
- Complete their work and zip up the working application.
- Applicants then need to send the ZIP archive to the email of the recruiting manager. This email be communicated to the
  applicant during the recruitment process.

  
---

## Additional Information based on the implementation

This section MUST be completed by applicants. It allows applicants to showcase their view on how an application
can/should be documented.
Applicants can choose to do this in a separate markdown file that needs to be included when the code is committed.

---

# Audition API - Implementation Details

The purpose of this Spring Boot application is to evaluate general knowledge of Spring Boot, Java, and Gradle.

## API Endpoints

### Get Posts

**URL:** `/posts`

**Method:** `GET`

**Query Parameters:**

- `userId` (optional, integer, minimum: 1): Filter posts by user ID
- `id` (optional, integer, minimum: 1): Filter posts by post ID
- `title` (optional, string): Filter posts by title
- `body` (optional, string): Filter posts by body

**Response:**

- `200 OK`: Returns a list of posts
- `204 No Content`: No posts found with the given parameters

### Get Post by ID

**URL:** `/posts/{id}`

**Method:** `GET`

**Path Parameters:**

- `id` (required, string, pattern: `\d+`): The ID of the post to retrieve

**Response:**

- `200 OK`: Returns the post with the specified ID
- `204 No Content`: No post found with the specified ID

### Get Comments for Post

**URL:** `/posts/{id}/comments`

**Method:** `GET`

**Path Parameters:**

- `id` (required, string, pattern: `\d+`): The ID of the post to retrieve comments for

**Response:**

- `200 OK`: Returns a list of comments for the specified post
- `204 No Content`: No comments found for the specified post

### Get Comments by Post ID

**URL:** `/comments`

**Method:** `GET`

**Query Parameters:**

- `postId` (required, string, pattern: `\d+`): The ID of the post to retrieve comments for

**Response:**

- `200 OK`: Returns a list of comments for the specified post ID
- `204 No Content`: No comments found for the specified post ID

## Error Handling

The application uses custom exceptions and logging to handle errors. The following are the main error handling
mechanisms:

- `SystemException`: Used for system-level errors
- `HttpClientErrorException`: Handles HTTP client errors
- `RestClientException`: Handles unexpected errors during REST calls

## Observability

The application includes logging, tracing, and metrics instrumentation to ensure observability. The `AuditionLogger`
class is used for logging, and tracing is managed using the `Tracer` class. Micrometer is used for metrics collection
and monitoring.

## Actuator Endpoints

The application exposes several Actuator endpoints for monitoring and management:

- `/actuator/health` and `/actuator/info` are publicly accessible.
- All other Actuator endpoints require basic authentication.

## Security Configuration

The application has the following security configuration:

- All API endpoints under `/posts/**` and `/comments/**` are publicly accessible.
- Actuator endpoints `/actuator/health` and `/actuator/info` are publicly accessible, while other Actuator endpoints
  require basic authentication.

## Trace and Span Details

Trace and span details are added to the response headers to facilitate distributed tracing.

## Interceptors

The application employs interceptors to capture trace details and log the request and response information of
third-party services. The `LoggingInterceptor` and `TracingInterceptor` classes facilitate these functionalities.

## Logging

The application uses Logback for logging and provides three types of log appenders:

1. **console**: Logs are output to the console.
2. **flatfile**: Logs are written to a file with a rolling policy based on time.
3. **logstash**: Logs are written to a file in JSON format, suitable for log aggregation tools like Logstash.

The logstash appender includes trace and span IDs to facilitate distributed tracing.

## Code Quality Tools

The project uses the following code quality tools:

- **Checkstyle**: Ensures that the code adheres to a set of coding standards.
- **PMD**: Scans the source code for potential bugs, dead code, suboptimal code, and other issues.

These tools are configured in the `build.gradle` file and are automatically run during the build process to maintain
code quality.

## Unit Tests

The application includes unit tests to ensure code quality and functionality. The goal is to achieve a minimum of 80%
test coverage.