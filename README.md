# Actividad 10

#### 1. 🎬 ¿Qué ventajas ofrece el uso de la clase VideoView para reproducir videos en una aplicación Android?**Desarrollar una aplicación Android que funcione como una galería de fotos con las siguientes características:

La clase *VideoView* facilita mucho la reproducción de videos porque ya trae integrada toda la lógica necesaria para mostrar contenido multimedia sin tener que programar un reproductor desde cero.
Algunas de sus ventajas son:

- Permite reproducir videos fácilmente con solo indicar la URL o la ruta del archivo.
- Incluye controles básicos como play, pause y seek, sin tener que agregarlos manualmente.
- Se integra bien con los componentes de la interfaz y puede colocarse directamente en el layout XML.
- Soporta diferentes formatos de video comunes en Android (MP4, 3GP, etc.).

En resumen, VideoView simplifica el proceso y es ideal para aplicaciones que solo necesitan una reproducción básica de video.


#### 2. 📂 ¿Cuáles son los diferentes orígenes desde donde se puede reproducir audio y video en una aplicación Android?**Cada foto se mostrará en una tarjeta con un diseño atractivo.

En Android se pueden reproducir archivos multimedia desde distintos orígenes, por ejemplo:

1. Archivos locales almacenados en la memoria interna o externa del dispositivo.

2. Recursos del proyecto (por ejemplo, dentro de la carpeta res/raw).

3. Flujos en línea (streaming), como videos o audios de Internet usando una URL.

4. Contenido capturado por el usuario, como grabaciones de audio o video hechas con la cámara o el micrófono.

#### 3. 🛠️ Menciona al menos cuatro clases de Android que permiten acceder a los servicios multimedia y describe brevemente su función.**Desplazamiento horizontal: el usuario podrá desplazarse por las fotos de forma horizontal deslizando el dedo sobre la pantalla.

🎵 Cuatro clases de Android que permiten acceder a los servicios multimedia

1. *MediaPlayer*
Permite reproducir archivos de audio y video. Es muy útil para controlar la reproducción (iniciar, pausar, detener, etc.) y manejar eventos como cuando termina el sonido.

2. *VideoView*
Es una vista lista para usar que combina MediaPlayer y SurfaceView para mostrar videos fácilmente en la interfaz.

3. *MediaRecorder*
Sirve para grabar audio y video desde el micrófono o la cámara del dispositivo.

4. *AudioManager*
Permite controlar el volumen, los modos de audio (silencio, llamada, multimedia) y gestionar el enfoque del audio entre distintas aplicaciones.

#### 4. 💭 **Reflexión Personal del Tema** *(mínimo 50 palabras)*Tarjetas: se utilizarán tarjetas para mostrar cada foto. Las tarjetas tendrán un diseño atractivo que incluya la imagen, el título y la descripción de la foto.

En lo personal, aprender sobre las clases multimedia en Android me pareció muy interesante porque me permitió entender cómo una aplicación puede reproducir audio y video de manera sencilla. Me di cuenta de que herramientas como VideoView y MediaPlayer facilitan mucho el trabajo del programador. Además, comprender los diferentes orígenes de los archivos multimedia me hizo valorar la importancia de optimizar los recursos y cuidar la experiencia del usuario al reproducir contenido desde el dispositivo o Internet.
