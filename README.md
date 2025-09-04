# 📘 ChazaAPI

**ChazaAPI** is a Java library designed to streamline the creation of API documentation by **automating the generation of clear, human-readable, and machine-processable documentation** directly from your source code.

The name **Chaza** means “explain” in Zulu, reflecting the library’s mission to clearly explain your API to its users.

Currently, ChazaAPI integrates with the **Javalin** web framework, allowing developers to embed documentation within their existing applications. Support for additional frameworks is planned in future releases.

---

## ✨ Features

- ✅ **Annotation-Based Documentation**  
  Document your API using simple Java annotations — directly in your controller methods.

- 🔄 **Support for All HTTP Methods**  
  Supports all standard REST HTTP methods: `GET`, `POST`, `PUT`, `DELETE`, and `PATCH`.

- 🔐 **Role-Based Access Control Documentation**  
  Annotate endpoints with required user roles to include security metadata in your docs.

- 📦 **Flexible Content Type Support**  
  Works with various content types (e.g., `application/json`) for both request and response bodies.

- 🛠️ **Automatic Documentation Generation**  
  Automatically generates documentation during your Maven build process — always up to date.

- 🌐 **Javalin Integration**  
  Works seamlessly with **Javalin v5+** (Recommended: **v6.7.0** or higher).

- 🧩 **Fluent API Metadata Configuration**  
  Set global metadata like title, version, license, and contact info using a clean, fluent API.

---

## ✅ Requirements

- **Java**: Minimum version **17** (Recommended: **21+**)  
- **Javalin**: Minimum version **5.x** (Recommended: **6.7.0+**)

---

## 📦 Installation

Add the following Maven dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.mabrikado</groupId>
  <artifactId>ChazaAPI</artifactId>
  <version>1.0.1</version>
</dependency>
```
## 🚀 Getting Started

Follow the official tutorial to set up and use ChazaAPI in your project:  
📖 [https://chazaapidocs.netlify.app/tutorial/](https://chazaapidocs.netlify.app/tutorial/)

This guide covers:

- Project setup and configuration
- Adding documentation annotations
- Serving the generated documentation with Javalin

---

## 📂 How It Works

ChazaAPI uses Java annotations to collect metadata about your APIs. This includes:

- 📌 API-wide metadata: title, description, version, contact, license
- 🔍 Endpoint metadata: HTTP method, path, request/response fields, status codes, security roles

During your Maven build, these annotations are processed to generate comprehensive documentation that can be served via your Javalin app.

---

## 🛡 License

ChazaAPI is licensed under the **Apache License 2.0** — you are free to use, modify, and distribute the library in both open-source and commercial projects.

```java
/**
 * ChazaAPI - A Javalin-based API documentation generator.
 *
 * Copyright (c) 2025 Sibusiso Buthelezi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
```
## 👤 Author

**Created by**: Sibusiso Buthelezi  
🔗 GitHub: [@mabrikado](https://github.com/mabrikado)

---

## 🤝 Contributing

Contributions are welcome! If you’d like to contribute to ChazaAPI, please fork the repository and open a pull request. Guidelines coming soon.

