# Numerical Analysis Lab - Full Project Documentation

## 📌 Executive Summary
**Numerical Analysis Lab** is a high-performance Android application developed using **Jetpack Compose** and **Kotlin**. It serves as a digital laboratory for solving complex mathematical and algebraic problems through numerical methods. The project emphasizes **academic accuracy**, **step-by-step transparency**, and a **premium user experience** through modern design principles.

---

## 🚀 Evolution & Version History
### Version 0.1 (Foundational)
- Core implementation of the **mXparser** engine.
- Basic functional screens for Root-Finding and Linear Algebra.
- Standard Material Design components.

### Version 0.2 (Stitch Overhaul - Current)
- **Glassmorphism UI:** Implementation of semi-transparent surfaces, backdrop filters, and high-precision borders.
- **Enhanced UX:** Introduction of the Dashboard for better navigation.
- **Comparison Engine:** Side-by-side analysis of different algorithms on the same problem.
- **Reporting:** Export capabilities to professional formats (PDF/Excel).

---

## 🛠 Feature Deep-Dive

### 1. Root-Finding Module (`f(x) = 0`)
The app supports finding roots for non-linear equations with high precision.
- **Algorithms:**
    - **Newton-Raphson:** Uses iterative derivatives ($x_{n+1} = x_n - f(x_n)/f'(x_n)$).
    - **Bisection:** A robust bracketing method for continuous functions.
    - **Secant:** A quasi-Newton method that doesn't require analytical derivatives.
    - **False Position (Regula Falsi):** Faster convergence than Bisection using linear interpolation.
    - **Fixed Point Iteration:** Solving equations in the form $x = g(x)$.
- **Visualization:** Real-time function plotting with iteration markers.

### 2. Linear Algebra Solver (`[A]{x} = {b}`)
Handles systems of linear equations with a dynamic matrix interface.
- **Solvers:**
    - **Gaussian Elimination:** With partial pivoting for stability.
    - **LU Decomposition:** Factors $A = LU$ for efficient solving.
    - **Gauss-Jordan:** Complete reduction to reduced row-echelon form.
    - **Cramer's Rule:** Using determinant ratios (ideal for smaller systems).
- **Interface:** A custom-built `MatrixGrid` that expands dynamically from $2\times2$ to $6\times6$.

### 3. Comparison Mode
Allows users to compare two methods simultaneously.
- **Metrics Tracked:**
    - **Execution Time (ms):** Benchmarking algorithm speed.
    - **Iteration Count:** Evaluating convergence efficiency.
    - **Precision Level:** Final absolute/relative error.

### 4. Data Export System
- **PDF Reports:** Generates professional-grade documents including calculation steps and final results.
- **Excel (XLSX):** Exports raw iteration data for further analysis in spreadsheet software.

---

## 🏗 Technical Architecture (MVVM)

### **1. Data Layer (`data/`)**
- **Models:**
    - `IterationStep`: Holds data for a single step in root-finding (xr, f(xr), error).
    - `MatrixStep`: Holds a snapshot of a matrix state during linear algebra reduction.
- **Repositories:** Contains the pure mathematical implementation of algorithms, isolated from Android dependencies.

### **2. UI Layer (`ui/`)**
- **Screens:** Composables that represent full views (e.g., `RootFindingScreen.kt`).
- **Components:** Atomic UI parts like `EquationInput`, `MatrixCell`, and the `GlassCard` wrapper.
- **Theme:** Specialized `Color.kt` and `Type.kt` defining the "Stitch" design language.

### **3. Utility Layer (`util/`)**
- **`MathEvaluator`:** Wraps **mXparser** to provide safe and efficient expression evaluation and range calculations for graphing.
- **`ExportManager`:** Handles the complexity of Android's `MediaStore` and `Apache POI` for saving files to the device.

---

## 🧰 Technology Stack
- **Jetpack Compose:** Declarative UI for high-speed development.
- **mXparser:** A robust library for parsing and calculating mathematical expressions.
- **Vico:** A powerful, extensible graphing library for Compose.
- **Apache POI:** Industry-standard library for manipulating Excel files.
- **Navigation Compose:** Type-safe routing between screens.
- **Coroutines:** Background processing for intensive mathematical computations.

---

## 📁 Key File Map
- `MainActivity.kt`: Entry point and Bottom Navigation orchestration.
- `NavGraph.kt`: Routing logic.
- `Color.kt`: Material 3 / Glassmorphism palette definitions.
- `RootFindingRepositoryImpl.kt`: The "Brain" of iterative solvers.
- `LinearAlgebraRepositoryImpl.kt`: The "Brain" of matrix operations.

---

## 📝 Developer Guidelines for Future Expansion
1. **Consistency:** All new screens should use the `GlassCard` component to maintain the design language.
2. **Performance:** For graphing, use `MathEvaluator.evaluateRange` which reuses argument objects for speed.
3. **Accuracy:** Use `Double` for all calculations, but format to 6-10 decimal places for UI display.
4. **Modularity:** Keep math logic in `repositories` and state in `viewmodels`.

---

## 🎯 Target & Use Cases
- **Educational:** Verifying homework for Numerical Methods courses.
- **Engineering:** Quick estimation of roots for design parameters.
- **Algorithmic Study:** Comparing the convergence rate of open vs. closed methods.
