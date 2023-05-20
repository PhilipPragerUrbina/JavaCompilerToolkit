# JavaCompilerToolkit(JCT)
A WIP project to make rapid prototyping of robust programming languages possible.
This project aims to be flexible and allow full control, while still making trivial cases easy.
It is a toolbox, you pick an choose what you want to use. It tries to be one size fits most, but if the shirt doesnt fit, you can at least still wear the pants.
It can be used for:
  - JIT languages(JVM, SIR JIT, etc.)
  - Interpreted languages
  - Transpilers
  - Compiled languages to common targets(LLVM, x86, etc)
  - Compiled languages to uncommon targets(GPU, custom hardware)
## Planned Components
JCT is made up of multiple main interoperable parts
1. Lexicographer: Frontend toolkit
  - Similar to ANTLR
  - Specify grammar with JSON file or some grammar definition langauges 
  - Generate a parser and lexer in java, or dynamically interpret grammars.
  - Helpful tools for parsing command line arguments, managing includes
  - May generate syntax highlighting plugins
2. Straighforward Intermediary Representation(SIR)
  - Compile to a variety of targets (LLVM, JVM, custom target, etc.)
  - Special features for interpreted or JIT languages
  - Comes with it's own JIT
  - Human readable and bytecode format
  - Inuitive API for generation from AST
  - Optimizations
3. Extras
  - Unified Error and warning handling with formatting. Can even be customized to show exceptions wherever.
  - Potentially might have some dependency managent stuff
  - File IO and other utilities
