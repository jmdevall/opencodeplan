Prueba de concepto-implementation de este paper en java: https://huggingface.co/papers/2309.12499

ATENCIÓN: este proyecto aún está incompleto. No es funcional. Sin embargo, se pueden ver ciertas partes y hay componentes sueltos. Por ahora la definición perfecta es que este proyecto es un caos.


He intentado seguir una arquitectura límpia, con el fin de poder cambiar las partes que no son propiamente del algoritmo. Dichas partes externas son:

- el llm
- el repositorio de código
- el parseador/analizador de dependencias

He seguido la estructura de paquetes sugerida aquí:
https://tbuss.de/posts/2023/9-how-to-do-the-package-structure-in-a-ports-and-adapter-architecture/

El objetivo es separar las partes que son del algoritmo de las que no, de modo que quien quiera pueda reaprovechar lo máximo posible.

FAQ:
¿Cual es tu pretensión con este proyecto?

Personalmente he estado probando otros otros proyectos que usan la IA y hacen de asistentes o son capaces de generar código. Como por ejemplo:

https://github.com/paul-gauthier/aider, https://github.com/genia-dev/GeniA etc

La idea fundamental del paper "codeplan" es aprovechar la información de las dependencias del propio proyecto para generar el prompt mas concreto posible. Hacer cambios pequeños poco a poco a lo largo de la base del código.
La dificultad que parece que se encuentran los asistentes actualmente es: 
- 1 conseguir reunir la información relevante para hacer un cambio. 
- 2 Plasmar el resultado del llm como cambio de vuelta en el repositorio.
El paper parece que ha solucionado estos dos problemas: sugiere usar la propia información semántica del lenguaje para obtener las dependencias de las partes implicadas en el código. Esto junto a los cambios anteriores, permitirían obtener el prompt perfecto. Por otro lado, al realizar cambios pequeños que solo afectan al código de una clase a la vez, en principio es mas sencillo trasladar esos cambios de vuelva al repositorio, a la vez que sirven para categorizar el tipo de cambio y así retroalimentar el prompt en futuras modificaciones.

De cualquier forma, la conclusión que parece que prevalece es que, cuanto mas pequeño es el cambio y mas controlado está, mas sencillo es aprovechar/interpretar el resultado del LLM, mas fácil es seguir un plan metódico que permita trabajar con proyectos grandes. En el paper se sugiere un algoritmo, codeplan, pero yo sugiero ir mas alla: sobre esta misma idea se podría aplicar algún otro algoritmo... por ejemplo... el algoritmo que sigue la metodología del Test Driven Development o lo que dicte el plan generado por un agente en un nivel superior para guiar el desarrollo.
De cualquier forma, mi única pretensión es que esto pueda servir de ayuda o inspiración para cualquier otro proyecto

¿Por que java y no python?
Por varios motivos:
1 Estoy mas acostumbrado a trabajar con java
2 Java es un lenguaje ampliamente utilizado en proyectos del mundo empresarial. En la práctica creo que sería el lenguaje que mas se podría aprovechar.
3 Java es un lenguaje fuertemente tipado. Tal como indica el paper, el algoritmo es mas apropiado y facil de implementar en este tipo de lenguajes.
4 Python parece mas adecuado para el mundo particular de la inteligencia artificial... podría parecer que es mas sencillo de implementar en python. Sin embargo realmente no utilizo nada extraodinario que requiera ningúna librería especializada. Para mi el llm es simplemente un servicio web externo al que le metes un String y te devuelve otro String. Lo mas complejo en este caso es el parser de java y obtener las dependencias. Además, como quería reaprovechar algo ya hecho, había pocas opciones mas alla de la librería javaparser.

¿Por qué usas javaparser y no usas tree-setter como parseador tal como indica el paper?
Ese fue mi primer intento, pero me he encontrado con 2 problemas:
1) La versión para java está linkada usando una librería nativa. En mi caso no conseguí que me funcionara, me daba core dumps y no encontré el motivo.
2) Ademas del parser, para encontrar las dependencias es muy util que la librería tambien implemente un "typesolver". Esto ya te lo da javaparser mientras que para tree-setter creo que no hay nada.

Por supuesto, si quieres colaborar, participar, sugerir cosas, preguntar lo que quieras... esto es un proyecto libre 


