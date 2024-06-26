# Poke Api

# Instrucciones Generales:
Lee cuidadosamente cada pregunta antes de comenzar.
La prueba debe completarse en el tiempo asignado.
Utiliza el lenguaje de programación Kotlin.
Asegúrate de seguir las mejores prácticas de desarrollo de Android.
Proporciona comentarios en tu código cuando sea necesario.
Utiliza la API de PokeAPI (https://pokeapi.co/) para obtener datos de Pokémon.

# Pregunta 1: Patrón MVVM y Retrofit
- [x] 1.1 Crea un proyecto Android que implemente la arquitectura MVVM.
- [x] 1.2 Conecta la aplicación a la PokeAPI utilizando Retrofit para obtener una lista de Pokémon.
- [x] 1.3 Muestra la lista en una actividad o fragmento utilizando RecyclerView, mostrando al menos el nombre y la imagen de cada Pokémon de 15 registros inicialmente.

# Pregunta 2: Almacenamiento local y Unit Testing
- [x] 2.1 Implementa una funcionalidad para almacenar la lista de Pokémon descargada en una base de datos local.
- [x] 2.2 Escribe pruebas unitarias para verificar que la lista se almacena y recupera correctamente desde la base de datos local.

# Pregunta 3: Trabajo en segundo plano y Notificaciones
- [x] 3.1 Implementa un servicio en segundo plano que actualice la lista de Pokémon cada 30 segundos con 10 nuevos Pokémon.
- [x] 3.2 Muestra una notificación cuando se actualice la lista en segundo plano, informando al usuario sobre la actualización.

# Pregunta 4: Excepciones y Manejo de Errores
- [x] 4.1 Maneja posibles excepciones que puedan ocurrir durante la descarga de la lista de Pokémon o el almacenamiento local.
- [x] 4.2 Implementa un mecanismo para informar al usuario en caso de que la descarga o el almacenamiento fallen.

# Pregunta 5: Diseño y UI/UX
- [x] 5.1 Mejora el diseño de la interfaz de usuario utilizando Material Design y considerando buenas prácticas de UI/UX.
- [x] 5.2 Implementa alguna forma de retroalimentación visual cuando se esté realizando la descarga de la lista de Pokémon.

# Pregunta 6: Bonus (opcional)
- [x] 6.1 Implementa una funcionalidad de búsqueda para filtrar la lista de Pokémon según un criterio específico como por generación o tipo.

# Capturas

- Pantalla de inicio
  ![Pantalla de inicio](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura1.jpg)

- Pantalla de inicio - Boton para cargar mas pokemon (al hacer scroll hasta abajo)
  ![ Boton para cargar mas pokemon ](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura2.jpg)

- Pantalla de inicio - Filtros
  ![Filtros](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura3.jpg)

- Pantalla de inicio - Mensaje de guardado
  ![Mensaje de guardado](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura4.jpg)

- Notificacion - Notificacion de nuevos pokemon
  ![Notificacion de nuevos pokemon](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura5.jpg)

- Pantalla de inicio - Cargando pokemon
  ![Cargando pokemon](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura6.jpg)

- Pantalla de inicio - Error al actualizar pokemon (error al descargar)
  ![Error al actualizar pokemon](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura7.jpg)

- Pantalla de inicio - Lista vacia
  ![Lista vacia](https://github.com/Drubico/Pokeapi-Test/blob/master/img/captura8.jpg)
