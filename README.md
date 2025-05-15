# PL9 - Programming Langauge 9

> Read some type theory at [9T56](https://github.com/chuigda/9T56).

## Features
- ML-alike syntax, with many tweaks (removing curry, more "modern" function call syntax, for example)
- HM type system, with type inference and type checking.
- Imperative first design (`loop`/`break`/`continue`/`return` control flow, `begin` - `end` block, `:=` and `*` syntactical sugar), also with functional flavor.
- Interact with JVM ecosystem, importing Java classes and functions using `native` types and functions.
- Limited interaction with Java OOP (subclassing polymorphism), not breaking compatibility with HM type system.
  - `class AbstractClass` and `class ConcreteClass extends AbstractClass` are still two distinct types in PL9's view. A PL9 variable of type `AbstractClass` may actually hold a `ConcreteClass` object, and when you call a function on `AbstractClass`, dynamic dispatch would work correctly. But PL9's type system would not allow you to call `ConcreteClass`'s methods directly on `AbstractClass` type variable (and not vice versa). So this is still standard HM type system, without any dangerous extension.
- Compile to JVM bytecode for performance, or direct interpreting for faster startup/lower memory footprint.
- Embeddable into existing Java/Kotlin programs via PL9 Java APIs, serving as configuration scripts/logic scripts.
- Visual Studio Code support.
- Embeddable text editor support (using RSyntaxArea).

## Considering features
- Typeclasses: this is good and compatible with our current design, but takes much effort to fulfill. This might be implemented in the second major edition PL9M.
- Pattern matching: this is a very powerful feature, but also takes effort to fulfill. We will provide a simpler version in the first edition, and might implement a more powerful version in the second major edition PL9M.
- Intellij family IDE support: Intellij family IDEs use `PsiElement`s. The core developer only has experience with VSCode LSPs before. But if this is not too hard, we might implement this soon.

## Non-goals
- Pure-functional: such languages (Lambda calculus for example) mostly serves as a theoretical model for research purpose, and are not practical for real-world programming. We want to be practical.
- Curry: syntactic sugar that looks tasty at the first glance, but actually makes the language harder to use (imagine you counting the number of arguments to find out how many parameters are curry-ed!). An alternative syntactical sugar -- partial apply (used by Scala) may be provided in the future.
- Implement full subclassing polymorphism: this would be overly complicated, and can lead to undecidable type inference problems.
