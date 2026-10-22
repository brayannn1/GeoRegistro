# GeoRegistro

Aplicacion movil en android desarrollada en java, el objetivo de la aplicacion
es permitir registrar y visualizar las incidencias georreferenciadas, utilizando
OpenStreetMap para verlas y FireBase para guardar las incidencias

## Tecnologías utilizadas

- **Java** 
- **XML** 
- **Android Studio** 
- **Firebase Authentication** 
- **Cloud Firestore** 
- **OpenStreetMap** 
- **OSMDroid** 

## Funcionalidades

- Registro e inicio de sesion de usuarios.
- Formulario para registrar incidencias.
- Validacion de campos obligatorios.
- Seleccion manual de la ubicación mediante un mapa.
- Almacenamiento de las incidencias en Cloud Firestore.
- Visualizacion de una incidencia en el mapa mediante un marcador.

## Requisitos

Para ejecutar el proyecto se necesita:

- Android Studio.
- JDK compatible con la versión de Android Gradle Plugin utilizada por el proyecto.
- Android SDK correspondiente a la configuración del proyecto.
- Una cuenta de Firebase.
- Un dispositivo Android o emulador con conexión a Internet.

## Instalación y configuración

1. Clonar el repositorio desde GitHub o descargar el proyecto.
2. Crear un proyecto en Firebase.
3. Habilitar el método de autenticación **Correo electrónico y contraseña** en Firebase Authentication.
4. Crear una base de datos en Cloud Firestore.
5. Descargar el archivo `google-services.json` y colocarlo dentro de la carpeta `app/`.
6. Sincronizar el proyecto con Gradle y ejecutar la aplicación en un dispositivo o emulador Android.


## Seguridad

La aplicación utiliza Firebase Authentication para identificar a los usuarios y Cloud Firestore para almacenar las incidencias. Las reglas de seguridad de Firestore deben restringir el acceso a los datos según el usuario autenticado.

## Pruebas

Se hicieron varias pruebas, una por cada ves que se implementaba una funcion nueva, la primera de navegacion sin logica aun, la segunda luego de crear validaciones al inicio de sesion como tambien de registro, luego varias para creacion de incidencias, verificacion de campos, tomar la hora actual, editar fecha y hora, guardar ubicacion actual. En la pantalla de mapa, prueba para consultar permisos, que corresponda la ubicacion actual. Por ultimo en la pantalla de incidencias, una prueba para validar que me redireccione a la pantalla de mapa.  

## Uso de inteligencia artificial

Se uso AI para ayuda con la autenticacion de firebase, para creacion de IncidenciaAdapter y SeleccionarUbicacion y como herramienta de apoyo, ademas de ayuda con la implementeacion de OpenStreetMap ya que tuve un problema con Google Cloud console.
