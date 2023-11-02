Prueba de concepto-implementation de este paper en java:


Estructura del proyecto: se ha seguido la estructura de paquetes sugerida aquí: https://tbuss.de/posts/2023/9-how-to-do-the-package-structure-in-a-ports-and-adapter-architecture/

The implementation is done using a clean architecture, so you could replace all the external details. Can
replace the repository implementation, the llm and the parser.

My first attempt was to use tree-setter as the parser suggests, but there is two problems:
1) the version for java is linked using a native library that for me does not work.
2) Para encontrar las relaciones entra For that you should need a dynamic library. The tests I do give me a core dump that I couldn't debug.

There are 2 layers:
The inner and outer core.

The core is a domain model and services that implement the use cases and interfaces with the outside world. Interfaces are what the hexagonal architecture calls "ports." I name it in the packages port.in and port.out

The outer layer is made up of adapters that implement all the details. There is a simple UI, but you can be prepared to create a plugin to integrate it into common IDEs (eclipse, vscode, etc., etc.)...

The internal implementation is very clean and does not depend on any libraries, so it can be relatively easy to translate the implementation to another language, such as Python, etc., etc.

FAQ: Why java and not python? 
1 Estoy mas acostumbrado a trabajar con java
2 Sería muy util al ser java un lenguaje ampliamente utilizado.
2 Parece que es mas sencillo de implementar: Realmente no utilizo nada extraodinario. Para mi un llm es simplemente un servicio web externo. Lo mas complejo realmente es crear toda la infraestructura para el analizador sintáctico del lenguaje.

