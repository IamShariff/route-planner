# 🚇 Delhi Metro Route Planner - Backend

A fully functional **Metro Route Planner** backend application developed using **Spring Boot**. This project allows users to:

- 📍 **Find all possible routes** between two metro stations.  
- 🎟️ **Buy metro tickets** with QR generation.  
- ⏰ **Apply discounts** during off-peak hours automatically.  
- 🛡️ **User authentication & registration** with JWT (JSON Web Token) using Spring Security.  
- 👩‍💻 **Admin access** for managing metro lines and stations.  

---

## 🛠️ Features  

- **User Registration & Login** using Spring Security with JWT.  
- **Find routes** between any two metro stations in Delhi.  
- **Buy metro tickets** and generate QR codes for easy access.  
- **Use tickets at entry and exit gates** by uploading QR codes.  
- **Dynamic Discounts** based on off-peak hours to reduce travel costs.  
- **Admin Panel** to manage metro lines and stations.  

---

## 🚀 Getting Started  

### 1. Prerequisites  

- **Java 17+**  
- **Maven 3+**  
- **PostgreSQL (pgAdmin)**  

---

### 2. Installation  

```bash
# Clone the repository
git clone https://github.com/username/delhi-metro-route-planner.git

# Navigate into the project directory
cd delhi-metro-route-planner

# Build the project
mvn clean install

## ⚙️ Configuration  

### Database Configuration  
Update the `application.properties` file with your PostgreSQL username, password, and database URL.  

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/metrodb
spring.datasource.username=your_pg_username
spring.datasource.password=your_pg_password
spring.jpa.hibernate.ddl-auto=update

## 🔒 Security  

- **User authentication** is managed through **Spring Security** and **JWT**.  
- Only **authenticated users** can:  
  - 🎟️ **Buy tickets**  
  - 📍 **Search routes**  
  - 🚉 **Use tickets at stations**  
- **Admin privileges** are required to:  
  - 🛠️ **Manage metro lines and stations**  

## 🔑 Key Endpoints  

| **Method** | **Endpoint**                       | **Description**                             | **Access**  |  
|------------|------------------------------------|---------------------------------------------|-------------|  
| **POST**   | `/user/add`                        | Register a new user                         | Public      |  
| **POST**   | `/user/authenticate`               | Login and receive JWT                       | Public      |  
| **POST**   | `/metro/station`                   | Add a new metro station                     | Admin       |  
| **GET**    | `/metro/station/{stationId}`       | Get station details by ID                   | Public      |  
| **POST**   | `/route/findRoutes`                | Find routes between two stations            | User        |  
| **POST**   | `/ticket/buy-ticket`               | Buy a metro ticket                          | User        |  
| **POST**   | `/ticket/upload-qr-source`         | Validate QR at source station               | User        |  
| **POST**   | `/ticket/upload-qr-destination`    | Validate QR at destination station          | User        |  

---

## 🚀 How it Works  

### 1. User Registration & Login  
- Users register through the **`/user/add`** endpoint.  
- JWT tokens are issued for successful login and must be used for subsequent API requests.  

### 2. Finding Routes  
- Users can search for routes between stations via **`/route/findRoutes`**.  
- The system returns the best paths, including interchanges if necessary.  

### 3. Buying Tickets  
- Tickets are bought through the **`/ticket/buy-ticket`** endpoint.  
- A **QR code** is generated, representing the ticket details.  

### 4. Using Tickets  
- Users upload QR codes at the source or destination station to validate their journey.  
- The system automatically applies **discounts for off-peak hours**.  

## 🛠️ Admin Functionality  

- **Admins can:**  
  - Add/Edit/Delete stations.  
  - Create and manage metro lines.  
  - View user travel history.  

---

## 💸 Discounts  

- **Peak Hours:** No discount applied.  
- **Off-Peak Hours:** 20% discount automatically applied.  

---

## 🤝 Contributing  

Feel free to **open issues** and submit **pull requests** for new features and bug fixes.  
Your contributions are highly appreciated! 🚀  
