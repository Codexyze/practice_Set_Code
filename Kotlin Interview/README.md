# Kotlin & Android Interview Masterclass Guide

Welcome to the **Kotlin & Android Interview Masterclass**. This repository contains a deeply structured, heavily detailed guide designed to take you from foundational programming concepts to advanced Object-Oriented Kotlin. 

This codebase is crafted specifically for Android developers looking to clear technical interviews or simply elevate their understanding of Kotlin's "under the hood" mechanics. It shifts the focus from simply *knowing the syntax* to truly *understanding the architectural philosophy* behind modern Android development (like Jetpack Compose, Coroutines, and Clean Architecture).

---

## 📚 Module Breakdown: What Each Module Does

### **Module 0: Programming Model Foundations**
Before jumping into syntax, this module fixes your mental model. It explains what a program actually is, why bugs happen, and why modern Android (especially Compose) heavily relies on immutability and pure functions.

### **Module 1: Core Kotlin Language**
This module dives deep into the bread and butter of Kotlin. It explains how Kotlin handles variables in memory, why it prefers expressions over statements, how functions are first-class citizens, and how Kotlin's null safety actually works under the hood to prevent the "Billion Dollar Mistake."

### **Module 2: Classes, Objects, Properties & Constructors**
This module transitions into Object-Oriented Kotlin. It is the foundation for modeling real-world data and architectural components (ViewModels, Repositories, UiState, etc.). You will learn exactly what the compiler generates for properties, how data classes differ from normal classes, how inheritance and sealed classes shape state modeling, and how singletons and companion objects replace Java's `static`.

---

## 🗺️ Topic Navigation Guide

Use the breakdown below to navigate through the chapters and specific topics.

### Module 0 — Programming Model Foundations
* **Chapter 0.1** — What Is a Program Really? (Data, Rules, State)
* **Chapter 0.2** — Values vs Variables & Mutable State
* **Chapter 0.3** — Immutability (Why Compose and MVI love it)
* **Chapter 0.4** — References vs Objects
* **Chapter 0.5** — Value vs Reference Thinking
* **Chapter 0.6** — Stack vs Heap Memory Concepts
* **Chapter 0.7** — Pure Functions vs Side Effects
* **Chapter 0.8** — Why Kotlin Exists (Solving Java's limitations)

### [Module 1 — Core Kotlin Language]
* **Chapter 1.1 — Type System & Memory**
  * `var` vs `val` (Reference vs Object mutability)
  * Type Inference & Static Typing
  * Boxing vs Unboxing (Why `Int?` is more expensive than `Int`)
  * Structural Equality (`==`) vs Referential Equality (`===`)
* **Chapter 1.2 — Expressions & Control Flow**
  * Statements vs Expressions (Why Kotlin prefers expressions)
  * Advanced `when` statements & `if` expressions
  * Smart Casts & Type checks
  * Ranges (`..` vs `until`), Loops, and Labels
  * The `Nothing` Type
* **Chapter 1.3 — Functions Deep Dive**
  * Function Anatomy, Parameters vs Arguments, and the `Unit` type
  * Single Expression Functions & Default/Named Arguments
  * Top-Level, Local, Infix, and Operator Functions
  * The famous `operator fun invoke()` pattern in Clean Architecture
* **Chapter 1.4 — Null Safety Deep Dive**
  * Nullable vs Non-Nullable Types
  * Safe Call (`?.`), Elvis Operator (`?:`), and `let` blocks
  * Non-Null Assertion (`!!`) and Platform Types (`String!`)
  * `requireNotNull` vs `checkNotNull`

### [Module 2 — Object-Oriented Kotlin]
* **Chapter 2.1 — Classes & Constructors**
  * Classes, Objects, and Properties
  * Primary vs Secondary Constructors & `init` Blocks
* **Chapter 2.2 — Properties & Backing Fields**
  * Properties vs Variables
  * Custom Getters and Setters
  * What the `field` keyword actually is
  * Backing Properties (The `_state` vs `state` pattern in ViewModels)
* **Chapter 2.3 — Data Classes & State**
  * What the compiler generates (`equals`, `hashCode`, `toString`)
  * The `copy()` function and immutable state updates
  * `componentN()` functions and Destructuring Declarations
* **Chapter 2.4 — Inheritance & Interfaces**
  * `open` classes, `override`, and the `super` keyword
  * Abstract Classes vs Interfaces (`IS-A` vs `CAN-DO`)
  * Polymorphism, Upcasting, and Downcasting (`as`, `as?`)
  * Sealed Classes and Interfaces (Modeling `UiState` and Network Results)
* **Chapter 2.5 — Objects & Singletons**
  * The `object` keyword (Singleton pattern)
  * `companion object` (Kotlin's alternative to Java's `static`)
  * Factory patterns & `const val`
  * Anonymous Objects / Object Expressions (`object : Listener {}`)
  * `data object` (Clean toString() for sealed hierarchies)