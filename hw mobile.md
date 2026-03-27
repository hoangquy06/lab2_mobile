**1. Project Overview**

You are tasked with building a simple 2D mobile game inspired by Flappy Bird on Android using Kotlin and Jetpack Compose. The application should focus on real-time rendering, basic physics simulation, and smooth user interaction.

The goal is NOT to clone the original game perfectly, but to implement a clean, minimal, and functional version demonstrating core game development concepts.

-----
**2. Required Skills (Capabilities Expected from the Agent)**

**Mobile Development**

- Kotlin (Android)
- Jetpack Compose (UI framework)
- Canvas rendering in Compose

**Game Development**

- Understanding of game loop (update → render cycle)
- Basic physics simulation (gravity, velocity, jump)
- Collision detection (rectangle-based)

**State Management**

- Reactive state handling in Compose
- Efficient recomposition control

**Performance Optimization**

- Frame rate control (~60 FPS)
- Avoid unnecessary recompositions
- Smooth animation handling
-----
**3. Functional Requirements**

**Core Gameplay**

- A bird character that:
  - Falls continuously due to gravity
  - Moves upward when user taps the screen
- Pipes:
  - Spawn from the right side of the screen
  - Move horizontally to the left
  - Have a gap for the bird to pass through
- Game Rules:
  - Game ends when the bird collides with a pipe or hits the ground
  - Score increases when the bird successfully passes a pipe
-----
**4. Technical Requirements**

**Game Loop**

- Implement a continuous loop using coroutine:
  - Target ~60 FPS (16ms per frame)
  - Separate update logic from rendering

**Physics System**

- Use simple physics:
  - velocity += gravity
  - position += velocity
- Tap input resets velocity upward

**Rendering**

- Use Jetpack Compose Canvas:
  - Draw bird (circle or image)
  - Draw pipes (rectangles)
  - Draw background (optional)

**Input Handling**

- Detect tap gestures
- Trigger bird jump action
-----
**5. Architecture Guidelines**

Structure the project as follows:

- GameScreen (entry UI)
- GameCanvas (rendering layer)
- GameState (single source of truth)
- Models:
  - Bird
  - Pipe
- Logic:
  - Physics update
  - Collision detection
  - Pipe spawning

Follow separation of concerns:

- UI handles rendering only
- Logic layer handles game mechanics
-----
**6. Non-Functional Requirements**

- Smooth gameplay (no visible lag)
- Maintainable and modular code
- Readable Kotlin code with clear structure
- Avoid memory leaks (stop loop when screen is disposed)
-----
**7. Deliverables**

The agent should produce:

1. Complete Android project (or core modules)
1. Playable game with:
   1. Tap to jump
   1. Moving pipes
   1. Collision detection
   1. Score tracking
1. Clean and modular code structure

