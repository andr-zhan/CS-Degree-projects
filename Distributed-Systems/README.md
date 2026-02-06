# 🌐 Distributed Systems

Contains the coursework projects for the Distributed Systems course.

---

## 📖 Overview

This course focuses on the design and implementation of distributed systems. The coursework is divided into two assignments:

- **Assignment 1:** Implementation of a distributed library management system using Java. The system includes client-server communication, database integration, and multi-threading.
- **Assignment 2:** Development of a distributed IoT monitoring system using gRPC, MQTT, REST, and Docker. The system simulates IoT devices and provides a scalable architecture for data collection and analysis.

---

## Contents

- [Assignment 1](./Assignment-1/)
  - `src/client/`: Client-side implementation.
  - `src/server/`: Server-side implementation.
  - `src/config/`: Configuration files.
  - `src/db/script.sql`: SQL script for database setup.
  - `docs/`: Documentation for Assignment 1.
- [Assignment 2](./Assignment-2/)
  - `src/admin-cli/`: Command-line interface for administrators.
  - `src/client-grpc/`: gRPC-based client implementation.
  - `src/client-mqtt/`: MQTT-based client implementation.
  - `src/client-rest/`: REST-based client implementation.
  - `src/server/`: Server-side implementation.
  - `src/proto/metrics.proto`: Protocol buffer definitions.
  - `docker-compose.yml`: Docker configuration for the system.
  - `docs/`: Documentation for Assignment 2.

---

## 🛠 Tech Stack

- **Languages:** Java, SQL
- **Technologies:** gRPC, MQTT, REST, Docker
- **Database Management System:** Any SQL-compatible DBMS
- **Operating System:** Cross-platform

---

## 🏗 Project Structure

```
Distributed-Systems/
│
├── Assignment-1/
│   ├── src/
│   │   ├── client/             # Client-side implementation
│   │   ├── server/             # Server-side implementation
│   │   ├── config/             # Configuration files
│   │   ├── db/script.sql       # SQL script for database setup
│   ├── docs/                   # Documentation for Assignment 1
│   └── README.md
│
├── Assignment-2/
│   ├── src/
│   │   ├── admin-cli/          # Command-line interface for administrators
│   │   ├── client-grpc/        # gRPC-based client implementation
│   │   ├── client-mqtt/        # MQTT-based client implementation
│   │   ├── client-rest/        # REST-based client implementation
│   │   ├── server/             # Server-side implementation
│   │   ├── proto/metrics.proto # Protocol buffer definitions
│   ├── docker-compose.yml      # Docker configuration for the system
│   ├── docs/                   # Documentation for Assignment 2
│   └── README.md
│
└── README.md                   # Course-level README
```

---

## ▶️ Usage

### Assignment 1

1. Set up the database using the provided SQL script (`src/db/script.sql`).
2. Configure the server and client using the files in `src/config/`.
3. Compile and run the server and client programs.

### Assignment 2

1. Use `docker-compose.yml` to set up the Docker environment.
2. Run the server and clients for gRPC, MQTT, and REST implementations.
3. Refer to the documentation for detailed instructions.

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05
