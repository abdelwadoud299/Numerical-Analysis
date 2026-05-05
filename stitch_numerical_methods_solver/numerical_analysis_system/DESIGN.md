---
name: Numerical Analysis System
colors:
  surface: '#f9f9ff'
  surface-dim: '#d9d9e1'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f3fa'
  surface-container: '#ededf5'
  surface-container-high: '#e7e8ef'
  surface-container-highest: '#e2e2e9'
  on-surface: '#191c21'
  on-surface-variant: '#424751'
  inverse-surface: '#2e3036'
  inverse-on-surface: '#f0f0f8'
  outline: '#737783'
  outline-variant: '#c2c6d3'
  surface-tint: '#255dad'
  primary: '#00346f'
  on-primary: '#ffffff'
  primary-container: '#004a99'
  on-primary-container: '#9bbdff'
  inverse-primary: '#abc7ff'
  secondary: '#016874'
  on-secondary: '#ffffff'
  secondary-container: '#9fecfa'
  on-secondary-container: '#0d6d79'
  tertiary: '#5e2300'
  on-tertiary: '#ffffff'
  tertiary-container: '#823400'
  on-tertiary-container: '#ffa87c'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d7e2ff'
  primary-fixed-dim: '#abc7ff'
  on-primary-fixed: '#001b3f'
  on-primary-fixed-variant: '#00458f'
  secondary-fixed: '#a2effd'
  secondary-fixed-dim: '#85d2e0'
  on-secondary-fixed: '#001f24'
  on-secondary-fixed-variant: '#004f58'
  tertiary-fixed: '#ffdbcb'
  tertiary-fixed-dim: '#ffb692'
  on-tertiary-fixed: '#341100'
  on-tertiary-fixed-variant: '#7a3000'
  background: '#f9f9ff'
  on-background: '#191c21'
  surface-variant: '#e2e2e9'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 57px
    fontWeight: '400'
    lineHeight: 64px
  headline-md:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  math-display:
    fontFamily: JetBrains Mono
    fontSize: 18px
    fontWeight: '500'
    lineHeight: 28px
  matrix-cell:
    fontFamily: JetBrains Mono
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-sm:
    fontFamily: Space Grotesk
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  margin-mobile: 16px
  gutter: 12px
---

## Brand & Style

This design system is engineered for precision, academic rigor, and professional utility. It targets researchers, engineers, and students who require a distraction-free environment for complex computation. 

The visual language blends **Material 3 (M3)** logic with a **Glassmorphic** aesthetic to soften the density of numerical data. The interface prioritizes clarity and high information density without sacrificing modern tactile feedback. The emotional response is one of "Technical Calm"—where the complexity of the math is balanced by the order and transparency of the UI. Use translucent layers to provide context of background calculations while focusing on the foreground task.

## Colors

The palette is rooted in "Science Blue" to establish institutional trust. "Deep Teal" serves as a secondary functional color for navigation and secondary actions. 

- **Primary (Science Blue):** Used for key interactive elements, FABs, and active states.
- **Secondary (Deep Teal):** Used for structural UI elements and filtering categories.
- **Accents (Electric Orange & Vivid Purple):** Reserved strictly for data visualization, error states (Orange), and complex mathematical constants or graph lines (Purple).
- **Surface Strategy:** Backgrounds use a very light neutral tint. Surfaces utilize semi-transparent white with backdrop filters to achieve the glassmorphic effect.

## Typography

This design system utilizes a dual-font approach to distinguish between "Interface" and "Data."

1.  **UI Elements:** Use **Inter** for all standard navigation, buttons, and body text. It provides the necessary neutrality for a professional tool.
2.  **Technical Labels:** Use **Space Grotesk** for small labels, tags, and technical metadata to lean into a scientific, geometric feel.
3.  **Mathematical Data:** Use **JetBrains Mono** for all equations, matrix inputs, and coordinate readouts. The monospaced nature ensures decimal points align vertically in tables and matrices, aiding rapid scanning of numerical results.

## Layout & Spacing

The layout follows a **Fluid Grid** system based on an 8px rhythm, with 4px increments for tight technical components. 

- **Margins:** Standard 16px lateral margins for mobile.
- **Cards:** Content is grouped into cards that span the full width or 50% width in landscape mode.
- **Density:** High-density layouts are preferred for data entry (matrices), while generous vertical spacing (24px+) is used to separate logical sections of a calculation report.

## Elevation & Depth

Hierarchy is established through **Glassmorphism** rather than traditional heavy shadows.

- **Level 1 (Base):** The main application background.
- **Level 2 (Cards):** M3 ElevatedCards with a white 70% opacity fill and a `blur(20px)` backdrop filter. Use a 1px solid inner border (white, 20% opacity) to define edges.
- **Level 3 (Modals/Sheets):** Bottom sheets and dialogs use higher opacity (90%) and a subtle ambient shadow (Blur: 15px, Opacity: 0.05, Color: Science Blue) to suggest they are floating closer to the user.
- **Level 4 (FAB):** Solid Science Blue, no transparency, with a standard M3 shadow to indicate primary action priority.

## Shapes

The design system employs **Rounded (0.5rem)** corners to balance the "sharpness" of mathematical data with a modern, approachable mobile feel.

- **Standard Containers:** 8px (0.5rem) radius.
- **Large Cards & Bottom Sheets:** 24px (1.5rem) top radius to emphasize the "container" feel.
- **Inputs:** 4px radius for a more "tooled" and precise appearance.
- **FABs:** Fully rounded (pill-shaped) to comply with Material 3 standards.

## Components

### Cards
Use **Material 3 ElevatedCards** as the primary container. Ensure the background blur is visible over the primary background. Titles inside cards should use Inter SemiBold.

### TextFields (Math-Optimized)
Input fields must support a "Math-Keyboard" suffix. When a field is focused, a specialized bottom sheet or accessory bar should appear containing symbols (∑, π, √, ∫). Use monospaced fonts for the input text.

### Floating Action Buttons (FAB)
The FAB is used for "Calculate" or "Add Variable." It should be the only component using the solid "Science Blue" without transparency to ensure it remains the focal point.

### Bottom Sheets
Used for configuration settings (e.g., changing integration methods like Trapezoidal vs. Simpson’s). Use a glassmorphic background for the sheet to maintain visual continuity with the cards below.

### Chips
Use Assist Chips for mathematical constants (e.g., *e*, *π*, *G*). Use Filter Chips to toggle between "Graph View" and "Table View."

### Data Grids/Matrices
Matrices should be rendered as a grid of low-outline TextFields. Use **JetBrains Mono** for the values. Highlight the active row/column using a very faint "Deep Teal" tint.