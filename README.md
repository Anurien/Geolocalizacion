# Juego de búsqueda del Tesoro con Geolocalización
Este proyecto es una aplicación de Android que permite jugar a un juego de búsqueda del Tesoro con geolocalización.
El objetivo del juego es encontrar diferentes pistas que se encuentran en lugares específicos del mapa.
Cuando el jugador se acerca a uno de estos lugares, se muestra una marca en el mapa que indica la ubicación de la pista.

## Requisitos
- Android Studio 
- API Key de Google Maps  
- Dispositivo Android o Emulador  

## Configuración
1. Clona el repositorio o descarga el código fuente.
En la carpeta raíz del proyecto, crea un archivo google_maps_api.xml dentro de la carpeta res/values/.
Este archivo debe contener tu API Key de Google Maps de la siguiente manera:

```
<resources>
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">TU_API_KEY_AQUÍ</string>
</resources>
```
2. Abre el proyecto en Android Studio y espera a que se descarguen las dependencias.  
3. Conecta tu dispositivo Android al ordenador o inicia un emulador.  
4. Ejecuta la aplicación desde Android Studio.  

## Cómo jugar

Para utilizar la aplicación, sigue los siguientes pasos:

1. Abre la aplicación en tu dispositivo Android.  
2. Espera a que la aplicación detecte tu ubicación actual.  
3. Explora el mapa para encontrar pistas.  
4. Cuando te acerques a una pista, aparecerá una marca en el mapa que indica su ubicación.  
5. Selecciona la marca en el mapa para obtener información sobre la pista.  

## Tecnologías
- Kotlin: Lenguaje de programación utilizado para desarrollar la aplicación.  
- Google Maps API: API de Google Maps utilizada para mostrar el mapa y las marcas en el mismo.  
- Android Studio: Entorno de desarrollo integrado utilizado para desarrollar la aplicación.  

## Capturas del juego

Pista cuando tocas una marca:  
![pista](https://user-images.githubusercontent.com/91197967/226068563-51e87764-11b9-4f22-af82-45728eee3c2a.png)


## Contribuciones
Si deseas contribuir a este proyecto, puedes crear un fork del repositorio y enviar tus cambios mediante un pull request.  
También puedes reportar problemas o sugerir mejoras utilizando la función de Issues de GitHub.
