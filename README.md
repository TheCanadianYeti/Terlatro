# Terlatro

> **Status:** Work in Progress (WIP) — Active development

A terminal-based poker roguelike engine built in Java, inspired by Balatro. Play classic hands, trigger dynamic Joker synergies, and scale chip and mult scoring against escalating blinds directly in your command line.

---

## Overview

Terlatro brings roguelike deckbuilding mechanics into a lightweight, zero-dependency Java CLI application. Players evaluate poker hands, exploit multiplier-stacking mechanics, manage shop economies, and purchase vouchers to survive consecutive rounds with increasing score thresholds.

---

## Key Features

- **Poker Hand Evaluation:** Evaluates standard hands (Pairs, Flushes, Full Houses, Straights) with dynamic base chip and multiplier scaling.
- **Synergy & Joker Engine:** Implements custom Joker cards that modify scoring equations, trigger conditional chips, and amplify multipliers during hand calculation.
- **In-Game Economy & Shop:** Spend earned prize money between rounds on booster packs, card enhancements, and voucher effects.
- **ANSI Terminal Interface:** Rendered with ANSI escape codes for stylized card suits, colors, and game status indicators.
- **Zero External Dependencies:** Built strictly using the Java Standard Library.

---

## System Architecture

The game utilizes core object-oriented principles to decouple the game loop, deck state, and scoring logic:
- `Deck` & `GroupOfCards`: Manages card generation, shuffling, hand draws, and discard piles.
- `HandEvaluator`: Inspects rank frequencies and suit distributions to determine the highest valid poker combination.
- `ScoreEngine`: Computes scoring curves: Final Score = Base Chips × Mult.
- `Joker`: Abstract base class enabling polymorphically evaluated triggers and persistent board effects.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher

### Build & Run

Compile the source files:
```bash
javac -d bin \$(find src -name "*.java")
```

Run the game:
```bash
java -cp bin ca.sheridancollege.project.Main
```

---

## Authors

<a href="https://marcus.podnar.ca" target="_blank" rel="noopener noreferrer">Marcus Podnar</a> | <a href="https://github.com/TheCanadianYeti" target="_blank" rel="noopener noreferrer">GitHub</a> | <a href="https://www.linkedin.com/in/marcus-podnar-582187260/" target="_blank" rel="noopener noreferrer">LinkedIn</a>

<a href="https://github.com/Puffmi" target="_blank" rel="noopener noreferrer">Jacob Milando</a>

<a href="https://github.com/Dark-Thorn" target="_blank" rel="noopener noreferrer">Griffin Tattongeyer</a>
