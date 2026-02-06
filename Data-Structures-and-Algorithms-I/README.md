# 🚀 Data Structures and Algorithms I

Contains the coursework project for the Data Structures and Algorithms I course.

---

## 📖 Overview

This project implements a spell checker using hash tables. The program reads a dictionary file and a text file, checks for spelling errors, and suggests corrections based on the dictionary. It demonstrates key concepts in data structures, including hash table implementation and string manipulation.

---

## Contents

- [Source Code](./src/)
- [Documentation](./docs/) (if available)
- [Input Files](./src/portuguese.txt, ./src/teste.txt)
- [Output File](./src/output.txt)

---

## 🛠 Tech Stack

- **Language:** C
- **Compiler:** GCC
- **Operating System:** Cross-platform

---

## 🏗 Project Structure

```
project-name/
│
├── src/
│   ├── hashTable.c      # Hash table implementation
│   ├── spellChecker.c   # Main program
│   ├── portuguese.txt   # Dictionary file
│   ├── teste.txt        # Input text file
│   ├── output.txt       # Output file with results
│   └── fatal.h          # Error handling
├── docs/                # Documentation
└── README.md
```

---

## ⚙️ Installation & Compilation

### Compile the Program

Navigate to the `src` directory and compile the program:

```bash
cd src
gcc -o spellChecker spellChecker.c hashTable.c -Wall -Wextra
```

---

## ▶️ Usage

1. Place the dictionary file (`portuguese.txt`) and the input text file (`teste.txt`) in the `src` directory.
2. Run the program:

```bash
./spellChecker
```

3. The results will be saved in `output.txt`.

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05
