# Project Brief: Numerical Lab - Mobile App

## 1. Project Overview
**Numerical Lab** is a high-fidelity scientific and educational mobile application designed for students, researchers, and engineers. It provides a robust suite of tools for numerical analysis, focusing on root-finding techniques and linear algebra solvers.

## 2. Core Aesthetic & Brand Identity
*   **Design Language:** Material 3 (Material You) with a clean, academic, and professional feel.
*   **Visual Style:** Glassmorphism and Card-based layout to organize complex mathematical data.
*   **Color Palette:**
    *   **Primary:** Science Blue (#004A99) - represent trust and precision.
    *   **Accents:** Electric Orange (Roots), Vivid Purple (Intersections).
    *   **Background:** Neutral off-whites and soft grays to maintain focus on content.
*   **Typography:**
    *   **UI Elements:** Inter (Sans-Serif) for readability.
    *   **Mathematical Data:** JetBrains Mono or Fira Code (Monospaced) for precise alignment of matrices and formulas.

## 3. Key Features & User Flows

### A. Dashboard & Discovery
*   Entry point for all numerical tools.
*   Categorized access to "Root-Finding Techniques" and "Linear Algebra Solvers."
*   Modern, intuitive iconography for quick navigation.

### B. Root-Finding Analysis
*   **Dynamic Input:** Math-friendly text field for f(x) with specialized character support.
*   **Parameter Control:** Sliders and fields for tolerance (ε), initial guesses, and maximum iterations.
*   **Algorithm Toggle:** Quick-switcher for methods like Newton-Raphson, Bisection, Secant, False Position, and Fixed Point.
*   **Step-by-Step Visualization:** Vertical "Timeline" view showing xi, error, and convergence status at each iteration.
*   **Interactive Graphing:** Embedded coordinate plane showing the function curve and animated markers for algorithm steps.

### C. Linear Algebra Solver
*   **Responsive Matrix Grid:** A spreadsheet-style n×n matrix input optimized for mobile touch.
*   **Process Flow:** Visualizing matrix transformations (e.g., Gauss Elimination) with highlighted pivot rows and calculated multipliers.

### D. Method Comparison
*   **Side-by-Side Analysis:** "Diff" style UI to compare convergence speeds and iteration counts between two algorithms.
*   **Performance Metrics:** Clear indicators of which method is most efficient for a given problem.

## 4. Technical Requirements & Components
*   **UI Components:** Material 3 TopAppBar, ElevatedCards, TextFields with suffix icons for math symbols.
*   **Interactions:** 
    *   Floating Action Button (FAB) for "Calculate."
    *   Bottom Sheet for export options (PDF/Excel).
    *   Micro-interactions for convergence success states.
*   **Exporting:** Ability to generate and share calculation reports.

## 5. Design Principles
*   **Clarity over Complexity:** High-density data must be broken down into digestible cards.
*   **Precision:** Mathematical results must never be truncated without user knowledge.
*   **Accessibility:** High-contrast colors for graph markers to assist in interpretation.
