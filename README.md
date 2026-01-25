# VitalScan - Campus Healthcare Management System

A comprehensive Spring Boot application for digitizing and streamlining campus healthcare with JWT authentication and role-based access control.

## Features

- **Multi-Identifier Login**: Users can login using Username, Roll Number, or Mobile Number
- **Role-Based Access Control**: Nurse (Admin), Student, and Staff roles
- **JWT Authentication**: Secure token-based authentication
- **Health Metrics Tracking**: Monitor vital signs and health history
- **Appointment Management**: Book, approve, and manage appointments
- **Inventory Management**: Track medicine stock with alerts
- **PostgreSQL Database**: Robust data storage with dev/prod configurations

## Technology Stack

- **Backend**: Spring Boot 3.5.10
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA with Hibernate
- **Validation**: Spring Boot Validation
- **Build Tool**: Maven

## Database Configuration

### Development Environment
- Database: `vitalscan_dev`
- Username: `vitalscan_dev`
- Password: `dev_password_123`
- Port: 5432

### Production Environment
- Database: `vitalscan_prod`
- Username: `vitalscan_prod`
- Password: `prod_secure_password_2024`
- Port: 5432

## Default Accounts

### Nurse (Admin)
- **Username**: nurse
- **Password**: nurse123
- **Email**: nurse@vitalscan.com
- **Roll Number**: NURSE001

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/validate` - Token validation

### Users
- `GET /api/users/current` - Get current user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/identifier/{identifier}` - Get user by identifier (username/roll/mobile)
- `GET /api/users` - Get all users (Nurse only)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Deactivate user (Nurse only)
- `GET /api/users/nurse` - Get nurse details

### Appointments
- `POST /api/appointments` - Create appointment
- `GET /api/appointments/my` - Get current user appointments
- `GET /api/appointments/patient/{patientId}` - Get patient appointments
- `GET /api/appointments/nurse` - Get nurse appointments (Nurse only)
- `GET /api/appointments/pending` - Get pending appointments (Nurse only)
- `GET /api/appointments/upcoming` - Get upcoming appointments
- `GET /api/appointments/{id}` - Get appointment by ID
- `PUT /api/appointments/{id}/approve` - Approve appointment (Nurse only)
- `PUT /api/appointments/{id}/update` - Update appointment (Nurse only)
- `PUT /api/appointments/{id}/cancel` - Cancel appointment

### Health Metrics
- `POST /api/health-metrics` - Create health metrics
- `POST /api/health-metrics/user/{userId}` - Create health metrics for user (Nurse only)
- `GET /api/health-metrics/my` - Get current user health metrics
- `GET /api/health-metrics/my/latest` - Get latest current user health metrics
- `GET /api/health-metrics/user/{userId}` - Get user health metrics
- `GET /api/health-metrics/user/{userId}/latest` - Get latest user health metrics
- `GET /api/health-metrics/user/{userId}/range` - Get health metrics by date range
- `PUT /api/health-metrics/{id}` - Update health metrics (Nurse only)
- `DELETE /api/health-metrics/{id}` - Delete health metrics (Nurse only)

### Inventory
- `POST /api/inventory` - Add medicine to inventory (Nurse only)
- `GET /api/inventory` - Get all inventory (Nurse only)
- `GET /api/inventory/{id}` - Get inventory by ID (Nurse only)
- `GET /api/inventory/medicine/{medicineName}` - Get inventory by medicine name (Nurse only)
- `GET /api/inventory/low-stock` - Get low stock items (Nurse only)
- `GET /api/inventory/out-of-stock` - Get out of stock items (Nurse only)
- `GET /api/inventory/status` - Get inventory status (Nurse only)
- `PUT /api/inventory/{id}` - Update inventory (Nurse only)
- `PUT /api/inventory/{id}/stock` - Update stock quantity (Nurse only)
- `PUT /api/inventory/medicine/{medicineName}/reduce` - Reduce stock (Nurse only)
- `DELETE /api/inventory/{id}` - Deactivate inventory item (Nurse only)

## Role-Based Access Control

### Nurse (Admin)
- Full access to all endpoints
- Can approve appointments
- Can manage inventory
- Can view all user data
- Can create health metrics for any user

### Student/Staff
- Can register and login
- Can book appointments
- Can view own health metrics and appointments
- Can update own profile
- Cannot access inventory or other users' data

## Running the Application

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Setup
1. Create PostgreSQL databases:
   ```sql
   CREATE DATABASE vitalscan_dev;
   CREATE DATABASE vitalscan_prod;
   ```

2. Update database credentials in `application-dev.properties` and `application-prod.properties`

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## JWT Configuration
- Secret: Configured in application.properties
- Expiration: 24 hours (production), 1 hour (development)
- Algorithm: HS256

## Security Headers
- Frame Options: DENY
- Content Type Options: nosniff
- XSS Protection: 1; mode=block

## Database Schema

### Users Table
- id, username, password, email, roll_number, mobile_number
- role, height, weight, first_name, last_name
- is_active, created_at, updated_at

### Health_Metrics Table
- id, user_id, height, weight, blood_pressure_systolic
- blood_pressure_diastolic, heart_rate, temperature
- oxygen_saturation, notes, recorded_at, created_at

### Appointments Table
- id, patient_id, nurse_id, appointment_date, status, type
- symptoms, diagnosis, prescription, notes
- created_at, updated_at

### Inventory Table
- id, medicine_name, description, current_stock, minimum_stock
- unit, manufacturer, expiry_date, batch_number
- is_active, created_at, updated_at
