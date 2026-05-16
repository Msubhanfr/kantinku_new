# Design System Strategy: The Fluid Canteen

## 1. Overview & Creative North Star
The objective of this design system is to elevate the "campus canteen" experience from a utility to a lifestyle. Our Creative North Star is **"The Kinetic Scholar."** 

We are moving away from the rigid, boxy layouts typical of academic software. Instead, we embrace an editorial approach characterized by **intentional asymmetry, overlapping organic layers, and high-contrast typography scales.** By treating the mobile screen as a canvas for a digital magazine rather than a spreadsheet of food items, we speak the language of a design-conscious student demographic. We break the "template" look by utilizing breathing room as a functional element, allowing the vibrant Turquoise and Orange palette to guide the eye through a rhythmic, non-linear flow.

## 2. Colors
Our palette is a dialogue between the calm, reliable `primary` (#2DAA9E) and the high-energy, appetizing `tertiary` (#FF8C42).

### The "No-Line" Rule
To achieve a premium editorial feel, **designers are prohibited from using 1px solid borders** to section content. Boundaries must be defined through tonal shifts. For example, a `surface-container-low` card should sit on a `surface` background. If you feel the need for a line, you haven't used your surface hierarchy effectively.

### Surface Hierarchy & Nesting
Treat the UI as a series of stacked, physical layers. 
- **Base Layer:** `surface` (#F8F9FA) for the main application background.
- **Content Blocks:** Use `surface-container-low` to define broad content areas.
- **Floating Interactive Elements:** Use `surface-container-lowest` (#FFFFFF) for cards or inputs that need to "pop." 
This nesting creates a soft, tactile depth that feels more sophisticated than a flat white screen.

### The "Glass & Gradient" Rule
To add "soul" to the digital interface:
- **Glassmorphism:** Use `surface_container_lowest` at 70% opacity with a `20px` backdrop blur for navigation bars and floating action headers. 
- **Signature Textures:** Main CTAs should not be flat. Apply a subtle linear gradient using the `primary` (#2DAA9E) palette to create a sense of curvature and premium finish.

## 3. Typography
We employ a "High-Low" typographic strategy:
- **The "High" (Display & Headlines):** Using **Plus Jakarta Sans**. This is our editorial voice—bold, youthful, and slightly wider. It should be used with generous leading and occasional negative letter-spacing (-2%) for large `display-lg` moments to create an authoritative, "brand-first" feel.
- **The "Low" (Body & Labels):** Using **Inter**. This is our workhorse. Inter provides the technical clarity needed for nutritional info, pricing, and button labels.

The contrast between the expressive Plus Jakarta Sans and the utilitarian Inter creates a "boutique" aesthetic that resonates with university students who value both style and efficiency.

## 4. Elevation & Depth
Hierarchy is achieved through **Tonal Layering** rather than structural scaffolding.

- **The Layering Principle:** Place a `surface-container-lowest` card on a `surface-container-low` section. This creates a natural "lift" that mimics fine stationery.
- **Ambient Shadows:** When an element must float (e.g., a checkout button), use an extra-diffused shadow. 
    - *Formula:* `Y: 8px, Blur: 24px, Spread: -4px`. 
    - *Color:* Use the `on_surface` color at 6% opacity. Never use pure black shadows; they look "dirty" on our clean base.
- **The "Ghost Border" Fallback:** If accessibility requirements demand a container edge, use the `outline_variant` token at **15% opacity**. It should be felt, not seen.
- **Depth through Blur:** Use backdrop blurs on `surface_variant` overlays to keep the user grounded in the "campus" context while focusing on a modal or cart selection.

## 5. Components

### Buttons
- **Primary:** Gradient-filled (utilizing `primary`), moderate (`2`) roundedness. Text in `on_primary`.
- **Secondary:** `surface_container_high` background with `primary` text. No border.
- **Tertiary (Accent):** Use `tertiary` (#FF8C42) for high-urgency hunger triggers like "Limited Time Offer."

### Cards & Lists
- **The "Zero Divider" Rule:** Forbid 1px dividers between food items. Use 16px of vertical white space or alternate between `surface` and `surface-container-low` backgrounds to separate items.
- **Card Styling:** Moderate (`2`) border radius. Use `surface-container-lowest` for the card body to make it "float" above the background.

### Input Fields
- **State Styling:** Use `surface-container-highest` for the field background. On focus, transition the background to `surface-container-lowest` and apply a "Ghost Border" using the `primary` color at 30% opacity. 

### Chips (Food Tags)
- **Selection Chips:** Use `secondary_container` for the background and `on_secondary_container` for text. The roundedness should be pill-shaped (3) to contrast against the moderate radius of the food cards.

### Navigation (The Floating Dock)
- Instead of a standard bottom nav bar, use a floating "dock" with a `24px` margin from the screen edges, utilizing the Glassmorphism rule (70% white + blur) to let the campus food imagery peek through.

## 6. Do’s and Don’ts

### Do:
- **Embrace Asymmetry:** Let food images bleed off the edge of the screen or overlap card boundaries to create energy.
- **Use Tonal Depth:** Use the full spectrum of `surface-container` tokens to guide the user's eye.
- **Prioritize "White Space":** Treat empty space as a premium design element, not "wasted" space.

### Don’t:
- **No Harsh Borders:** Never use 100% opaque `outline` colors for boxes or dividers.
- **No Default Shadows:** Avoid the standard "drop shadow" presets. Shadows must be ambient and tinted.
- **No Grids of Death:** Avoid perfectly symmetrical 2x2 grids for food items. Vary the sizes to maintain editorial interest.
- **No Pure Black:** Ensure all "black" text uses `on_surface` to maintain the soft, premium feel.
