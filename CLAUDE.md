# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a travel website project called "Trip YamYam" (trip_java_gwangju_04) that provides information about Korean tourist attractions, food, and festivals. The project is a Maven-based Java web application (WAR packaging) with a frontend web interface for searching and viewing travel destinations.

## Project Structure

This is a Maven web application with the following structure:

```
trip/
├── pom.xml                    # Maven configuration (Java 21, Jakarta EE)
├── src/main/webapp/           # Web application root
│   ├── index.html            # Main landing page
│   ├── html/                 # All HTML pages
│   │   ├── search.html       # Main search functionality page
│   │   ├── login.html        # User login page
│   │   ├── signup.html       # User registration page
│   │   ├── hotplace.html     # Popular destinations page
│   │   ├── mytrip.html       # User's saved trips page
│   │   ├── trip-planner.html # Trip planning page
│   │   └── user-manager.html # User management page
│   ├── js/                   # JavaScript modules
│   │   ├── navbar.js         # Navigation bar logic and user session management
│   │   ├── login.js          # Login form handling
│   │   ├── signup.js         # Registration form handling
│   │   ├── trip-planner.js   # Trip planning functionality
│   │   └── user-manager.js   # User data management utilities
│   ├── css/                  # Stylesheets
│   │   └── styles.css        # Main stylesheet
│   └── assets/               # Static assets (images, videos, icons)
├── src/main/java/            # Java source code (currently empty)
└── target/                   # Maven build output
```

## Development Setup

This is a Maven-based Java web application that uses:

- **Java 21** as the target runtime
- **Jakarta EE** (Servlet API 6.0.0, JSP API 4.0.0)
- **Maven** for build management and dependency resolution
- **WAR packaging** for deployment to servlet containers
- **Bootstrap 5.3.3** for frontend styling and responsive design
- **Font Awesome** for icons
- **Google Fonts** for typography

### Common Development Commands

```bash
# Build the project
mvn clean compile

# Package as WAR file
mvn clean package

# Run tests (when tests are added)
mvn test

# Clean build artifacts
mvn clean
```

### Running the Application

Since this is a Java web application (WAR), you need a servlet container:

1. **Using Maven with a servlet container plugin** (when configured)
2. **Deploy to Tomcat, Jetty, or similar servlet container**
3. **For development**: Since the project currently contains only frontend assets, you can run the webapp folder as a static site:
   ```bash
   cd src/main/webapp
   python -m http.server 8000
   # Or use any static file server
   ```

## Key Features

- **User Management**: Registration, login, logout, and session management
- **Search Functionality**: Location-based search with region and category filtering
- **Responsive Design**: Mobile-friendly interface using Bootstrap
- **Navigation**: Consistent navbar across all pages with user state management

## Code Architecture

### Frontend Architecture
- **Static HTML Pages**: Each major feature has its own HTML file
- **Modular JavaScript**: Functionality split into focused JS modules
- **Shared Navigation**: Common navbar with user session state management
- **Bootstrap Integration**: Responsive design using Bootstrap components

### User Session Management
The application manages user authentication state through:
- `navbar.js`: Handles login state display and navigation updates
- `login.js` & `signup.js`: Form validation and user authentication
- `user-manager.js`: User data persistence and management utilities

### Important Implementation Notes
- Currently frontend-only implementation with no Java backend code yet
- Maven project structure ready for Java backend development
- User data likely stored in localStorage or sessionStorage for frontend functionality
- Navigation state managed through JavaScript DOM manipulation
- All external frontend dependencies loaded via CDN
- Project uses Jakarta EE APIs, prepared for modern servlet container deployment

## File Naming Conventions
- HTML files use lowercase with hyphens (kebab-case)
- JavaScript files use lowercase with hyphens
- Korean language content in documentation and UI text