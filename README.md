ChazaAPI 📚✨

ChazaAPI is a lightweight, annotation-driven Java library designed to simplify the documentation of RESTful APIs. Inspired by the Zulu word “Chaza,” meaning “explain,” this library helps developers clearly and efficiently describe their APIs directly in the source code, creating synchronized, human-readable, and machine-processable API documentation.

What Does ChazaAPI Do? 🛠️

ChazaAPI focuses on making your API documentation seamless and tightly integrated with your codebase by providing two key capabilities:

1. Documenting API General Information ℹ️

This includes high-level metadata about your API that helps consumers understand what your API is, who maintains it, and the legal terms governing its use. With ChazaAPI, you can describe:

API Title: A concise name that identifies your API.

Version: The current version number of your API, indicating updates or changes.

Description: A brief summary of the API’s purpose and functionality.

Terms of Service: Legal terms or URL pointing to the terms that users must agree to.

Contact Information: Ways to reach out to the API maintainer or support team, such as email or phone.

License: Licensing details, including license name and URL, so users know how they can legally use the API.

These details establish important context and set expectations for anyone using the API.

2. Documenting API Endpoints 📍

Endpoints represent the specific operations your API provides — the various URLs and HTTP methods clients use to interact with your service. ChazaAPI allows you to annotate each endpoint method to include:

HTTP Method: The type of HTTP request handled, including GET, POST, PUT, DELETE, and PATCH.

URL Path: The relative path clients use to access the endpoint.

Group Name: Logical grouping or categorization of endpoints for better organization and readability.

Description: A clear explanation of what the endpoint does.

Request Fields: The data expected from clients, including field names and types.

Response Fields: The structure and types of data returned by the endpoint.

Status Codes: The HTTP status codes the endpoint might return along with their meanings (e.g., 200 OK, 401 Unauthorized).

Content Types: Supported media types such as application/json or text/plain.

Roles: Security roles or permissions required to access the endpoint.

By defining these properties right above your API handler methods, ChazaAPI ensures your documentation stays accurate and up to date with the actual implementation.

Key Features 🚀

Annotation-Based Approach: Easily add comprehensive documentation without writing external files or manually syncing docs.

Supports All Major HTTP Methods: Including GET, POST, PUT, DELETE, and PATCH for full REST API coverage.

Role-Based Documentation: Specify which user roles can access each endpoint for clarity and security auditing.

Flexible Content Types: Document endpoints with various content types to reflect real-world usage.

Automatic Generation and Hosting: Generate the full API documentation dynamically and serve it via your Javalin server, making it instantly available to developers and clients.

Integration with Javalin 6.7.0: Fully compatible with Javalin version 6.7.0 and Java 21, ensuring modern, reliable API development.

Fluent API Metadata Configuration: Conveniently configure your API’s metadata using fluent setters for clean, readable code.

How to Get Started 🚀

To begin documenting your API with ChazaAPI, follow these general steps:

Add Required Dependencies:
Include the ChazaAPI library and Javalin 6.7.0 in your project’s dependency management system to ensure compatibility.

Annotate Your API Methods:
Use ChazaAPI’s annotations on your handler methods to define endpoint details, request/response formats, status codes, and access roles.

Configure API Metadata:
Set your API’s title, version, description, contact, terms of service, and license information using the provided API metadata configuration.

Scan and Generate Documentation:
Scan your annotated classes to collect endpoint data and generate the full documentation.

Host the Documentation:
Serve the generated documentation through your Javalin server so it is accessible to your team and API consumers.

This workflow keeps your documentation accurate, easy to maintain, and directly tied to your actual code.

Why Use ChazaAPI? 🤔

Keep Docs and Code Together: By embedding documentation annotations directly in your source code, ChazaAPI eliminates the common problem of outdated or forgotten API documentation.

Clear Communication: Well-documented endpoints with detailed descriptions and request/response schemas make it easier for consumers to understand and correctly use your API.

Developer Productivity: Automating documentation generation saves time and reduces errors during development and deployment cycles.

Professional API Presentation: Provides a polished, standardized presentation of your API information suitable for internal teams or public users.

Legal Clarity: Including terms of service and licensing information helps establish clear usage boundaries and protects both maintainers and users.

Compatibility & Requirements ⚙️

Javalin version: Compatible with Javalin 6.7.0

Java version: Requires Java 21 or newer

Build Tools: Primarily designed for Maven, but can be adapted to other build tools with proper dependency management.

Licensing & Usage ⚖️

ChazaAPI is offered as free software. You have permission to use, copy, modify, merge, and distribute the library. However, the author is not liable for any damages, misuse, or illegal use of this software. Users must comply with local laws and regulations when using ChazaAPI in their projects.
