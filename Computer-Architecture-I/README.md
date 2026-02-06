# 🚀 Computer Architecture I

Contains the final project for the Computer Architecture I course.

---

## 📖 Overview

This project implements, in RISC-V Assembly, a program to remove noise from a 400x599 grayscale image using mean and median filters. It reads a `.gray` image file, applies the selected filter to reduce noise, and saves the filtered image.

---

## Contents

- [Source Code](./src/)
- [Documentation](./docs/)
- [Input Image](./assets/images/cat_noisy.png)
- [Output Image with Mean Filter](./assets/images/cat_mean_result.png)
- [Output Image with Median Filter](./assets/images/cat_median_result.png)

---

## 🛠 Tech Stack

- **Language:** RISC-V Assembly
- **Compiler / Runtime:** RARS (RISC-V Assembler and Runtime Simulator)
- **Operating System:** Cross-platform

---

## 🏗 Project Structure

```
project-name/
│
├── src/              # Source code
├── docs/             # Documentation (if available)
├── assets/images/    # Input and output images
└── README.md
```

---

## ⚙️ Installation & Compilation

### Open in RARS

1. Open the `trabalho_final.asm` file in the RARS simulator.
2. Assemble the program.
3. Run the program with the required arguments.

---

## ▶️ Usage

1. Place the input `.gray` image file in the `assets/images/` directory.
2. Run the program in RARS, specifying the filter type (mean or median).
3. The output image will be saved in the same directory.

Example:

- Input: `cat_noisy.gray`
- Output (Mean Filter): `cat_mean_result.png`
- Output (Median Filter): `cat_median_result.png`

---

## 👤 Authors

**André Zhan**
🔗 GitHub: https://github.com/andr-zhan

**André Gonçalves**
🔗 GitHub: https://github.com/andrefsg05


