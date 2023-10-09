Attempt to implement this paper in java


The implementation is done using clean architecture, so it would be able to replace all the external details, you can
replace the Repository implementation, the llm, and the parser.

My first try was to use tree-setter as the parser, but it could not be able to. For that I should need a dynamic library. The tests I do gives me a core dump that I was not able to debug.

There is 2 layers:
The internal core and the external

The core is a domain model, and services that implements the use cases and the interfaces to the outside world. The interfaces are what hexagonal architecture names "ports". I name it in packages port.in and port.out

The extenal layer is composed with adapters that implements all the details. There is a simple ui, but it may be prepared to create a plugin to be integrated in common IDEs (eclipse, vscode, etc etc)...

The internal implementation is very clean and do not depends on any library so it may be relatively simple to translate the implementation to another language, like python, etc etc.



The external 
core is 


