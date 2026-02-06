# 🖥️ Operating Systems

Contains the coursework projects for the Operating Systems course.

---

## 📖 Overview

This course focuses on the design and implementation of operating system components. The coursework is divided into two assignments:

- **Assignment 1:** Implementation of a process simulator, including queue management and process scheduling.
- **Assignment 2:** Development of memory management and process synchronization mechanisms, divided into two parts.

---

## Contents

- [Assignment 1](./Assignment-1/)
  - `src/simulador.c`: Main simulator program.
  - `src/queue.c` and `src/queue.h`: Queue management implementation.
  - `src/inputs.c`: Input handling.
  - `src/makefile`: Build instructions.
  - `docs/`: Documentation for Assignment 1.
- [Assignment 2](./Assignment-2/)
  - **Part 1:**
    - `Part1/main.c`: Main program for memory management.
    - `Part1/memoria.c` and `Part1/memoria.h`: Memory management implementation.
    - `Part1/queue.c` and `Part1/queue.h`: Queue management.
    - `Part1/Makefile`: Build instructions.
    - `Part1/outputs/`: Output files.
  - **Part 2:**
    - `Part2/simulador.c` and `Part2/simulador.h`: Process synchronization simulator.
    - `Part2/memoria.c` and `Part2/memoria.h`: Memory management implementation.
    - `Part2/queue.c` and `Part2/queue.h`: Queue management.
    - `Part2/Makefile`: Build instructions.
    - `Part2/outputs/`: Output files.

---

## 🛠 Tech Stack

- **Language:** C
- **Build System:** Makefile
- **Operating System:** Linux/Unix

---

## 🏗 Project Structure

```
Operating-Systems/
│
├── Assignment-1/
│   ├── src/
│   │   ├── simulador.c       # Main simulator program
│   │   ├── queue.c           # Queue management implementation
│   │   ├── queue.h           # Queue management header
│   │   ├── inputs.c          # Input handling
│   │   ├── makefile          # Build instructions
│   ├── docs/                 # Documentation for Assignment 1
│   └── README.md
│
├── Assignment-2/
│   ├── Part1/
│   │   ├── main.c            # Main program for memory management
│   │   ├── memoria.c         # Memory management implementation
│   │   ├── memoria.h         # Memory management header
│   │   ├── queue.c           # Queue management implementation
│   │   ├── queue.h           # Queue management header
│   │   ├── Makefile          # Build instructions
│   │   ├── outputs/          # Output files
│   ├── Part2/
│   │   ├── simulador.c       # Process synchronization simulator
│   │   ├── simulador.h       # Simulator header
│   │   ├── memoria.c         # Memory management implementation
│   │   ├── memoria.h         # Memory management header
│   │   ├── queue.c           # Queue management implementation
│   │   ├── queue.h           # Queue management header
│   │   ├── Makefile          # Build instructions
│   │   ├── outputs/          # Output files
│   ├── docs/                 # Documentation for Assignment 2
│   └── README.md
│
└── README.md                 # Course-level README
```

---

## ▶️ Usage

### Assignment 1

1. Navigate to the `Assignment-1/src/` directory.
2. Compile the program using the `makefile`:

```bash
make
```

3. Run the simulator:

```bash
./simulador
```

### Assignment 2

#### Part 1

1. Navigate to the `Assignment-2/Part1/` directory.
2. Compile the program using the `Makefile`:

```bash
make
```

3. Run the program:

```bash
./main
```

#### Part 2

1. Navigate to the `Assignment-2/Part2/` directory.
2. Compile the program using the `Makefile`:

```bash
make
```

3. Run the simulator:

```bash
./simulador
```

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05
