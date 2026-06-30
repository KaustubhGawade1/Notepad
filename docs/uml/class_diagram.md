# UML Class Diagrams

## 1. Backend JPA Entity Model

```mermaid
classDiagram
    direction LR
    
    class User {
        -Long id
        -String username
        -String password
        -List~Document~ documents
        +User()
        +getId() Long
        +setId(Long) void
        +getUsername() String
        +setUsername(String) void
        +getPassword() String
        +setPassword(String) void
        +getDocuments() List~Document~
        +setDocuments(List~Document~) void
    }
    
    class Document {
        -Long id
        -String name
        -String content
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -User owner
        +Document()
        +getId() Long
        +setId(Long) void
        +getName() String
        +setName(String) void
        +getContent() String
        +setContent(String) void
        +getCreatedAt() LocalDateTime
        +getUpdatedAt() LocalDateTime
        +getOwner() User
        +setOwner(User) void
        +onCreate() void
        +onUpdate() void
    }
    
    User "1" --> "*" Document : owns
```

## 2. Backend Service & Controller Layer

```mermaid
classDiagram
    direction LR
    
    class AuthController {
        -AuthService authService
        +register(AuthRequest) ResponseEntity~AuthResponse~
        +authenticate(AuthRequest) ResponseEntity~AuthResponse~
    }
    
    class DocumentController {
        -DocumentService documentService
        +listDocuments(Principal) ResponseEntity~List~DocumentResponse~~
        +getDocument(Long, Principal) ResponseEntity~DocumentResponse~
        +createDocument(DocumentRequest, Principal) ResponseEntity~DocumentResponse~
        +updateDocument(Long, DocumentRequest, Principal) ResponseEntity~DocumentResponse~
        +deleteDocument(Long, Principal) ResponseEntity~Void~
    }
    
    class AuthService {
        -UserRepository repository
        -PasswordEncoder passwordEncoder
        -JwtService jwtService
        -AuthenticationManager authenticationManager
        +register(AuthRequest) AuthResponse
        +authenticate(AuthRequest) AuthResponse
    }
    
    class DocumentService {
        -DocumentRepository documentRepository
        -UserRepository userRepository
        +listDocuments(String) List~DocumentResponse~
        +getDocument(Long, String) DocumentResponse
        +createDocument(DocumentRequest, String) DocumentResponse
        +updateDocument(Long, DocumentRequest, String) DocumentResponse
        +deleteDocument(Long, String) void
        -findUser(String) User
        -mapToResponse(Document) DocumentResponse
    }
    
    class JwtService {
        -String secretKey
        -long jwtExpiration
        +extractUsername(String) String
        +generateToken(UserDetails) String
        +isTokenValid(String, UserDetails) boolean
        -isTokenExpired(String) boolean
        -extractExpiration(String) Date
        -extractAllClaims(String) Claims
        -getSignInKey() Key
    }
    
    AuthController --> AuthService : uses
    DocumentController --> DocumentService : uses
    AuthService --> JwtService : uses
    DocumentService --> "UserRepository & DocumentRepository" : queries
```

## 3. Backend Repository Interfaces

```mermaid
classDiagram
    direction LR
    
    class UserRepository {
        <<interface>>
        +findByUsername(String) Optional~User~
    }
    
    class DocumentRepository {
        <<interface>>
        +findByOwnerId(Long) List~Document~
        +findByIdAndOwnerId(Long, Long) Optional~Document~
    }
    
    UserRepository --|> "JpaRepository~User, Long~" : extends
    DocumentRepository --|> "JpaRepository~Document, Long~" : extends
```

## 4. Backend DTO Model

```mermaid
classDiagram
    direction LR
    
    class AuthRequest {
        -String username
        -String password
    }
    
    class AuthResponse {
        -String token
    }
    
    class DocumentRequest {
        -String name
        -String content
    }
    
    class DocumentResponse {
        -Long id
        -String name
        -String content
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }
```

## 5. Client HTTP API Client

```mermaid
classDiagram
    direction LR
    
    class DocumentApiClient {
        -HttpClient httpClient
        -Gson gson
        -String jwtToken
        +isAuthenticated() boolean
        +login(String, String) void
        +register(String, String) void
        +listDocuments() List~CloudDocument~
        +getDocument(Long) CloudDocument
        +saveDocument(String, String) CloudDocument
        -sendAuthRequest(String, AuthRequest) AuthResponse
        -authenticatedRequest(String, String, Object) HttpRequest
        -send(HttpRequest) String
    }
    
    class CloudDocument {
        <<record>>
        -Long id
        -String name
        -String content
        -String createdAt
        -String updatedAt
    }
    
    class AuthRequest {
        <<record>>
        -String username
        -String password
    }
    
    class AuthResponse {
        <<record>>
        -String token
    }
    
    class DocumentRequest {
        <<record>>
        -String name
        -String content
    }
    
    DocumentApiClient --> CloudDocument : returns
    DocumentApiClient --> AuthRequest : sends
    DocumentApiClient --> AuthResponse : receives
    DocumentApiClient --> DocumentRequest : sends
```

## 6. Client UI & Dialog Layer

```mermaid
classDiagram
    direction LR
    
    class NotepadFrame {
        -EditorPanel editorPanel
        -StatusBar statusBar
        -MenuBarBuilder menuBarBuilder
        -ToolBarBuilder toolBarBuilder
        -CloudLoginDialog* findDialog
        -DocumentApiClient documentApiClient
        -EditorController controller
        +getEditorPanel() EditorPanel
        +getController() EditorController
        -initializeFrame() void
        -initializeShortcuts() void
    }
    
    class MenuBarBuilder {
        -EditorController controller
        -EditorPanel editorPanel
        -StatusBar statusBar
        -FindDialog findDialog
        -DocumentApiClient documentApiClient
        -Frame parentFrame
        +build() JMenuBar
    }
    
    class ToolBarBuilder {
        -EditorController controller
        -EditorPanel editorPanel
        -StatusBar statusBar
        -FindDialog findDialog
        -DocumentApiClient documentApiClient
        -Frame parentFrame
        +build() JToolBar
    }
    
    class CloudLoginDialog {
        -DocumentApiClient apiClient
        -JTextField usernameField
        -JPasswordField passwordField
        -JLabel statusLabel
        -authenticate(boolean) void
    }
    
    class CloudDocumentsDialog {
        -DocumentApiClient apiClient
        -EditorController controller
        -EditorPanel editorPanel
        -StatusBar statusBar
        -DefaultTableModel tableModel
        -JTable table
        -loadDocuments() void
        -openSelectedDocument() void
    }
    
    NotepadFrame --> MenuBarBuilder : creates
    NotepadFrame --> ToolBarBuilder : creates
    MenuBarBuilder --> CloudLoginDialog : creates
    MenuBarBuilder --> CloudDocumentsDialog : creates
    ToolBarBuilder --> CloudLoginDialog : creates
    ToolBarBuilder --> CloudDocumentsDialog : creates
    MenuBarBuilder --> DocumentApiClient : uses
    ToolBarBuilder --> DocumentApiClient : uses
    CloudLoginDialog --> DocumentApiClient : authenticates via
    CloudDocumentsDialog --> DocumentApiClient : queries via
```

## 7. Client Application Context (IoC)

```mermaid
classDiagram
    direction LR
    
    class ApplicationContext {
        -BeanFactory factory
        -BeanRegistry registry
        +ApplicationContext()
        +getBean(Class~T~) T
        -initializeBeans() void
    }
    
    class BeanFactory {
        +createEventManager() EventManager
        +createUndoManager() UndoManager
        +createFileManager() FileManager
        +createDocumentApiClient() DocumentApiClient
        +createDocument(EventManager) Document
    }
    
    class BeanRegistry {
        -Map~Class, Object~ beans
        +register(Class~T~, T) void
        +get(Class~T~) T
    }
    
    ApplicationContext --> BeanFactory : uses
    ApplicationContext --> BeanRegistry : uses
    BeanFactory --> "EventManager, UndoManager, FileManager, DocumentApiClient, Document" : creates
    BeanRegistry --> "EventManager, UndoManager, FileManager, DocumentApiClient, Document" : stores
```

## 8. Client Controller & Model

```mermaid
classDiagram
    direction LR
    
    class EditorController {
        -Document document
        -FileManager fileManager
        -UndoManager undoManager
        +clearDocument() void
        +saveDocument(String, String) void
        +openDocument(String) void
        +getText() String
        +setText(String) void
        +undo() void
        +redo() void
        +getDocument() Document
        +isModified() boolean
        +getDocumentName() String
        +markModified() void
        +markSaved() void
    }
    
    class Document {
        -String name
        -boolean modified
        -TextBuffer buffer
        +getName() String
        +setName(String) void
        +getBuffer() TextBuffer
        +isModified() boolean
        +setModified(boolean) void
    }
    
    class UndoManager {
        -Stack~Command~ undoStack
        -Stack~Command~ redoStack
        +execute(Command) void
        +undo() void
        +redo() void
        +clear() void
        +getUndoSize() int
        +getRedoSize() int
    }
    
    class FileManager {
        +save(Document, String) void
        +open(Document, String) void
    }
    
    EditorController --> Document : manages
    EditorController --> UndoManager : delegates
    EditorController --> FileManager : uses
    Document --> TextBuffer : contains
```
