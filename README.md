# AKB Chit Fund Application 💰

This application is a **Chit Fund Management System** designed to track user financial plans and manage scheme enrollments.

-----

## 💻 Technology Stack

| Component | Technology |
| :--- | :--- | :--- |
| **Frontend (Client)** | **React.js** |
| **Backend (Server)** | **Java Spring Boot** | 
| **Database** | **MySQL** | 
-----

## 💡 Purpose & Features

### Purpose

The core purpose is to provide clear tracking for two roles:

  * **For Users:** To easily keep track of all the **chit fund plans** they have opted into.
  * **For Admins:** To provide a comprehensive interface for managing and maintaining the details of all **users and schemes (schemas)**.

### Key Roles

| Role | Responsibilities |
| :--- | :--- |
| **Admin** | **Create and manage schemas (plans).** The Admin is the sole authority for adding users *into* a specific schema. |
| **User** | View and track their individual enrollment in various chit fund plans. |

-----

## 🚀 Setup & Installation

You can run this application natively or using Docker containers.

### 1\. Run as Native Application

Follow these steps to run the **Client (Frontend)** and **Server (Backend)** components separately.

#### 1.1. Prerequisites

  * React.js (v18)
  * Java Development Kit (JDK 17)
  * Apache Maven
  * MySQL Server

#### 1.2. Database Configuration (MySQL)

The Spring Boot server is configured to connect to a **MySQL** database.

1.  Start your local MySQL server.
2.  Create a database with the following default credentials (as configured in `application.properties`):
      * **Database Name:** `akb_chit_fund`
      * **User:** `akb`
      * **Password:** `akb-chit-fund`
      * ***Optional:*** If you use different credentials, you must update the `src/main/resources/application.properties` file in the server folder.

#### 1.3. Server (Backend - Java Spring Boot) Setup

1.  **Navigate to Server:**

    ```bash
    cd akb-chit-fund-app/server
    ```

2.  **Build the Project:**

    ```bash
    mvn clean install
    ```

    You should see a `BUILD SUCCESS` message.

3.  **Run the Application:**
    Execute the main class (e.g., `AkbChitFundApplication`) as a standard Java application:

    ```bash
    java -jar target/akb-chit-fund-application.jar 
    # OR run from your IDE
    ```

    The server will start on port **8082**, and the API documentation (Swagger UI) will be accessible at `http://localhost:8082/swagger-ui.html`.

#### 1.4. Client (Frontend - React.js) Setup

1.  **Navigate to Client:**
    ```bash
    cd akb-chit-fund-app/client
    ```
2.  **Install Dependencies:**
    ```bash
    npm install
    ```
3.  **Build and Run:**
      * First, build the optimized production files:
        ```bash
        npm run build
        ```
      * Then, launch the application:
        ```bash
        npm start
        ```
4.  The application should automatically open in your browser at `http://localhost:3000`.

-----

### 2\. Run as a Container (Docker)

This method simplifies the setup by running the application within Docker containers.

#### 2.1. Prerequisites

  * Docker and Docker Compose installed.

#### 2.2. Execution

1.  **Clone the Repository:**
    ```bash
    git clone [YOUR_REPO_URL]
    cd akb-chit-fund-app
    ```
2.  **Navigate to Server Folder:**
    The provided script assumes the server folder has the necessary `Dockerfile` and setup.
    ```bash
    cd server
    ```
3.  **Run the Setup Script:**
    This script is expected to build the Java application, create the Docker image, and launch the container (often linked to a separate local DB container).
    ```bash
    ./run.sh
    ```
4.  Access the **Client** at `http://localhost:3000` and the **API Server** at `http://localhost:8082`.
