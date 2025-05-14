# PL9 - Programming Langauge 9

## Features
- ML-alike syntax, with many tweaks (removing curry, more "modern" function call syntax, for example)
- HM type system, with type inference and type checking
- Imperative first design (`loop`/`break`/`continue`/`return` control flow, `begin` - `end` block, `:=` and `*` syntactical sugar) with functional flavor.
- Interact with JVM ecosystem, importing Java classes and functions using `native` types and functions
- Very limited interaction with Java OOP (subclassing polymorphism), not breaking compatibility with HM type system.
- Compile to JVM bytecode for performance, or direct interpreting for faster startup/lower memory footprint.
- Embeddable into existing Java/Kotlin programs via PL9 Java APIs, serving as configuration scripts/logic scripts.

## Considering features
- Typeclasses: this is good and compatible with our current design, but takes much effort to fulfill. This might be implemented in the second major edition PL9M.
- Pattern matching: this is a very powerful feature, but also takes effort to fulfill. We will provide a simpler version in the first edition, and might implement a more powerful version in the second major edition PL9M.

## Non-goals
- Pure-functional: such languages (Lambda calculus for example) mostly serves as a theoretical model, and are not practical for real-world programming. We want to be practical.
- Implement full subclassing polymorphism: this would be overly complicated, and can lead to undecidable type inference problems.
