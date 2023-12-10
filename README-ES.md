Prueba de concepto-implementation de este paper en java: https://huggingface.co/papers/2309.12499


**NOVEDADES**: 2023/12/04 Sigo corrigiendo bugs. Se invoca el llm para recuperar las partes a incrustar en el repositorio y se consigue obtener el contexto temporal para una segunda invocación y una tercera etc....Había un bug en el método getAffectedBlocks que estaba cogiendo el nodo destino en lugar del nodo origen para obtener los nodos afectados.

Puedes probar el test jmdevall.opencodeplan.application.CodePlanTest.otro3TestCodePlan que está funcionando: simplemente trata de renombrar un método. Como consecuencia hace una segunda llamada al llm para renombrar la interfaz de la que hereda el método. etc. etc.

Para depurar mejor he implementado dos adaptadores al llm:
* LlmEngineCacheAdapter: Utiliza una carpeta en local donde guarda las requests y responses en ficheros a modo de caché.
* LlmEngineDebugAdapter: guarda en una carpeta temporal del sistema cada una de las peticiones/respuestas que realiza al llm.

Issues: El código encargado del mezclado (que se encarga de mezclar el código revisado en el código podado). No había contemplado que el llm tratase de cambiar algunas líneas de algún método no afectado. El paper no especifica como hace este paso. 

Las pruebas las estoy haciendo en local arrancando el oobatextgen, usando distintos modelos. Desconozco como anda ahora mismo estado del arte.

https://huggingface.co/TheBloke/Phind-CodeLlama-34B-v2-GGUF -> Muuuy lento
TheBloke_Mistral-7B-Code-16K-qlora-GPTQ" que si cabe en la GPU -> No lo suficientemente bueno pero al menos cabe en la GPU


Actualmente solo dispongo de una tarjeta gráfica doméstica de 8 Gb por lo que solo puedo ejecutar modelos quantizados de 7B de parámetros en la GPU. Creo que es insuficiente, por lo que lo aconsejable sería ir a modelos mas avanzados de 33B, lo cual requeriría al menos una tarjeta de 24Gb...

python server.py --model phind-codellama-34b-v2.Q5_K_M.gguf --threads 12 --n_ctx 16384 --api --verbose

otro modelo usando exllama:
python server.py --model TheBloke_Mistral-7B-Code-16K-qlora-GPTQ --api --verbose
Una vez arrancado ooba levanta el api en el puerto 5000

ATENCIÓN: este proyecto aún está incompleto. No es funcional. Sin embargo, se pueden ver ciertas partes y hay componentes sueltos.

Como prueba de repositorio estoy usando otro proyecto mio https://github.com/jmdevall/nemofinder el cual tengo clonado en local.

En el test es necesario iniciar el proceso iterativo indicando un bloque y una instrucción. Por lo que dice el paper una instrucción puede ser de tipo Diff o una instrucción en lenguaje natural.
El bloque es complicado especificarlo ya que aún no se ha hecho parseo del código. He tendido que crearme una clase para describir el bloque (buscarlo una vez parseado el código).

Estructura del proyecto:
He intentado seguir una arquitectura límpia, con el fin de poder cambiar las partes que no son propiamente del algoritmo. Dichas partes externas son:

- el llm
- el repositorio de código
- el parseador/analizador de dependencias

En la parte de dominio se encuentran las estructuras de datos que maneja el algoritmo

En la parte de aplicación se encuentran los servicios... en esencia el algoritmo.

Por lo pronto no existe interfaz de usuario ni nada parecido. Todas las pruebas se hacen desde los tests

He seguido la estructura de paquetes sugerida aquí:
https://tbuss.de/posts/2023/9-how-to-do-the-package-structure-in-a-ports-and-adapter-architecture/

El objetivo es separar las partes que son del algoritmo de las que no, de modo que quien quiera pueda reaprovechar lo máximo posible.

FAQ:
¿Cual es tu pretensión con este proyecto?

El desarrollo está siendo lento puesto que solo dedico alguna parte de mi tiempo libre. Personalmente he estado probando otros otros proyectos que usan la IA y hacen de asistentes o son capaces de generar código. Como por ejemplo:

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


**TODO LIST**

- [x] Primer test funcionando
- [ ] bug en relaciones constructedby en fields (dado que los fields están recortados. Buscar forma alterntiva para hacer la búsqueda del nodo relacionado.
- [ ] Mejor documentación y explicación para que la comunidad pueda probarlo
- [ ] El algoritmo se ejecuta sobre otro proyecto java. Quizas debería crear una carpeta dentro del proyecto tal que se pudieran bajar los proyectos de prueba usando git (actualmente solo tengo en otra carpeta en local y para cada prueba tengo que andar restaurando el repositorio a mano.
- [x] Actualmente el objeto repository solo puede manejar una única carpeta de fuentes para el proyecto. Debería ser capaz de manejar multiples directorios de fuentes, por ejemplo, en proyectos maven hay una carpeta src/main/java y otra src/test/java. Los cambios que realiza el algoritmo afectan a clases de ambas.
- [ ] Al arrancar un modelo en ooba con exllama, parece que ooba expone en el puerto 5000 otro api diferente. Segun parece es el api compatible con openai.Sería recomendable en ese caso hacer otra implementación del servicio rest compatible con openai.
- [ ] Implementar el oracle. Si es sobre un proyecto maven, debería ser capaz de invocarlo para que pase los tests y el resultado de los tests pasárselo al prompt.
- [ ] El paper en los modificaciones de métodos habla de hacer un análisis de "escaping object"