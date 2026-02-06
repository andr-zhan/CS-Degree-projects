# 🚀 Computer Networks

Contains the coursework project for the Computer Networks course.

---

## 📖 Overview

This project implements a client-server architecture for a messaging application. The system supports user authentication, group management, message exchange, and file transfers. It is designed to demonstrate key concepts in computer networking, including socket programming, concurrency, and protocol design.

---

## Contents

- [Client Source Code](./src/client/)
- [Server Source Code](./src/server/)
- [Data Base](./src/server/Database)
- [Documentation](./docs/)

---

## 🛠 Tech Stack

- **Language:** C
- **Compiler:** GCC
- **Operating System:** Linux / Cross-platform

---

## 🏗 Project Structure

```
project-name/
│
├── src/
│   ├── client/        # Client-side implementation
│   ├── server/        # Server-side implementation
│   └── server/Database/ # User and group data
├── docs/             # Documentation
└── README.md
```

---

## ⚙️ Installation & Compilation

### Compile the Client and Server

Navigate to the respective directories and use the provided `makefile`:

```bash
# Compile the client
cd src/client
make

# Compile the server
cd ../server
make
```

---

## ▶️ Usage

### Start the Server

1. Navigate to the `src/server` directory.
2. Run the server executable:

```bash
./server
```

### Start the Client

1. Navigate to the `src/client` directory.
2. Run the client executable:

```bash
./client
```

### Features

- **User Authentication:** Login and registration functionality.
- **Group Management:** Create, join, and manage groups.
- **Messaging:** Send and receive messages in real-time.
- **File Transfer:** Upload and download files between clients and the server.

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05

**Miguel Pombeiro**
🔗 GitHub: https://github.com/MiguelPombeiro

**Miguel Rocha**
🔗 GitHub: https://github.com/miguelrocha1
