# NotepadVibe Architecture

## System Overview

NotepadVibe is a two-tier Swing desktop application with cloud synchronization capabilities. Users can create, edit, and save notes locally, or authenticate with a cloud backend to store, retrieve, and manage documents across sessions.

### Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                   Swing Desktop Client                      │
│   (GUI, Local File I/O, Event Management, Undo/Redo)       │
├─────────────────────────────────────────────────────────────┤
│              HttpClient (Java 21 HTTP/2)                    │
│            Gson JSON Serialization/Deserialization          │
├─────────────────────────────────────────────────────────────┤
│            Spring Boot REST Backend (Port 8080)             │
│   (JWT Authentication, Document CRUD, PostgreSQL JPA)       │
├─────────────────────────────────────────────────────────────┤
│              PostgreSQL Database                            │
│         (Users, Documents with Ownership Model)             │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### 1. Client-Side (notepad-client)

#### GUI Components
- **NotepadFrame**: Main application window, orchestrates menu/toolbar builders and initializes cloud API client
- **EditorPanel**: Text editing area with undo/redo support via UndoManager
- **StatusBar**: Displays document metadata (name, modified status, word count)
- **ToolBarBuilder**: Constructs toolbar with local file operations and cloud actions
- **MenuBarBuilder**: Constructs menu bar with File, Edit, Search, and cloud authentication menus

#### Cloud Integration
- **DocumentApiClient**: HTTP client using Java 21 `java.net.http.HttpClient`
  - Stores JWT token in memory after login/register
  - Attaches `Authorization: Bearer <token>` header to all document requests
  - Handles authentication and CRUD operations over REST
  - Uses Gson for JSON serialization/deserialization

- **CloudLoginDialog**: Modal dialog for username/password entry
  - Supports both login and registration flows
  - Stores token in DocumentApiClient upon success

- **CloudDocumentsDialog**: Table view of user's cloud documents
  - Fetches document list from backend
  - Allows users to open cloud documents into the editor

#### Local Features
- **EditorController**: Manages local document state and undo/redo operations
- **FileManager**: Handles local file I/O (load/save)
- **UndoManager**: In-memory undo/redo stack via Command pattern
- **EventManager**: Publishes document change events (used by status listeners)

#### Application Context (IoC)
- **ApplicationContext**: Simple bean container
- **BeanFactory**: Creates service instances
- **BeanRegistry**: Stores and retrieves beans by type

### 2. Backend (notepad-backend)

#### Authentication Flow
- **AuthController** (`/api/auth/register`, `/api/auth/login`)
  - Accepts username/password credentials
  - Returns JWT token with 24-hour expiration

- **JwtService**: Token generation and validation
  - Uses HS256 algorithm with Base64-encoded secret key
  - Extracts and validates claims

- **SecurityConfig**: Spring Security configuration
  - Permits unauthenticated access to `/api/auth/**`
  - Requires JWT authentication for `/api/documents/**`
  - Stateless session management (no cookies)

- **JwtAuthenticationFilter**: Per-request JWT extraction and validation
  - Parses `Authorization: Bearer <token>` header
  - Sets authenticated context in Spring Security

#### Document Management
- **DocumentController** (`/api/documents/**`)
  - GET `/api/documents` — List user's documents (paginated)
  - GET `/api/documents/{id}` — Fetch single document
  - POST `/api/documents` — Create new document
  - PUT `/api/documents/{id}` — Update document
  - DELETE `/api/documents/{id}` — Delete document
  - All endpoints extract username from JWT for ownership verification

- **DocumentService**: Business logic for document CRUD
  - Enforces ownership: users can only read/modify their own documents
  - Manages timestamps (createdAt, updatedAt)
  - Delegates persistence to repositories

#### Data Model
- **User** (JPA Entity)
  - Fields: `id`, `username`, `password` (BCrypt-hashed), `documents` (relationship)
  - Unique constraint on `username`
  - Cascading delete to documents

- **Document** (JPA Entity)
  - Fields: `id`, `name`, `content`, `createdAt`, `updatedAt`, `owner` (FK)
  - Timestamps auto-managed via `@PrePersist` and `@PreUpdate`
  - Lazy-loaded owner relationship (avoids JSON serialization loops)

- **AuthRequest/AuthResponse**: DTO for auth endpoints
- **DocumentRequest/DocumentResponse**: DTO for document endpoints

#### Repositories
- **UserRepository**: Extends `JpaRepository<User, Long>`
  - `findByUsername(username)` — lookup by username
  
- **DocumentRepository**: Extends `JpaRepository<Document, Long>`
  - `findByOwnerId(ownerId)` — list documents by owner
  - `findByIdAndOwnerId(id, ownerId)` — verify ownership before CRUD

### 3. Database (PostgreSQL)

#### Schema
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE documents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Data Flow

### Scenario 1: Local Editing
1. User edits text in **EditorPanel**
2. **TextBuffer** captures change → **EventManager** publishes **TextChangedEvent**
3. Event listeners update **StatusBar** (word count, modified indicator)
4. User presses Ctrl+S or clicks Save → **FileManager** writes to local file

### Scenario 2: Cloud Authentication
1. User clicks "Login to Cloud" → **CloudLoginDialog** appears
2. User enters credentials → dialog calls `documentApiClient.login(username, password)`
3. **DocumentApiClient** sends POST to `/api/auth/login` with JSON credentials
4. Backend validates, returns JWT token in response
5. Token stored in **DocumentApiClient** memory, UI buttons enabled
6. Subsequent requests attach `Authorization: Bearer <token>` header

### Scenario 3: Cloud Document Save
1. User clicks "Save to Cloud"
2. Prompt for document name (defaults to local document name)
3. **DocumentApiClient** sends POST to `/api/documents` with name and content
4. Backend:
   - Extracts username from JWT
   - Creates Document entity with owner = User
   - Saves to PostgreSQL with timestamps
   - Returns DocumentResponse
5. Local editor updates with cloud document metadata

### Scenario 4: Cloud Document Open
1. User clicks "Open from Cloud" → **CloudDocumentsDialog** fetches documents
2. Backend returns list of documents filtered by authenticated user (`findByOwnerId`)
3. Dialog displays table with id, name, createdAt, updatedAt
4. User selects row → dialog calls `documentApiClient.getDocument(id)`
5. Backend verifies ownership (`findByIdAndOwnerId`), returns DocumentResponse
6. Dialog loads content into **EditorPanel**, updates **EditorController**

## Security Considerations

### Authentication
- Passwords hashed via BCrypt (never stored in plaintext)
- JWT tokens signed with HS256 algorithm
- Token expiration: 24 hours (configurable via `jwt.expiration`)

### Authorization
- Document endpoints require valid JWT
- Ownership verified at service layer (users cannot access others' documents)
- Stateless: no session objects, reduced server memory overhead

### Data Integrity
- Foreign key constraints prevent orphaned documents
- Cascading deletes remove documents when user deleted
- Timestamps auto-managed for audit trail

## Configuration

### Backend Configuration (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notepad_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
jwt.secret=<Base64-encoded-256-bit-key>
jwt.expiration=86400000  # 24 hours in ms
```

### Client Configuration (hardcoded)
```java
private static final String BASE_URL = "http://localhost:8080";
```

## Deployment Notes

### Backend
1. Ensure PostgreSQL is running on `localhost:5432`
2. Build: `mvn clean package`
3. Run: `java -jar target/notepad-backend-0.0.1-SNAPSHOT.jar`
4. Server listens on `http://localhost:8080`

### Client
1. Build: `mvn clean package`
2. Run: `java -jar target/notepad-client-1.0-SNAPSHOT-jar-with-dependencies.jar`
3. Requires JDK 21 (uses record types and new HttpClient API)

## Extensions & Future Work

- **Multi-user collaboration**: WebSocket support for real-time editing
- **Document versioning**: Store history of document changes
- **Rich text support**: Format text (bold, italic, font selection)
- **Search/Replace with regex**: Backend-indexed full-text search
- **Mobile client**: Kotlin Multiplatform or Android native app
- **OAuth2 integration**: SSO via Google/GitHub
