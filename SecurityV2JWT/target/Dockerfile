#toma la version actual de java en la que creara la imagen
FROM amazoncorretto:17-alpine-jdk

#crea una nueva carpeta en donde se creara la imagen
#WORKDIR /app

#copia el jar que contiene todo lo necesario para correr la app
#junto con las dependencias y recursos compilados
#posteriormente le indicamos en que ruta lo debe copiar
COPY SecurityV2JWT-0.0.1-SNAPSHOT.jar app.jar

#indica la forma o el medio que inicializara la app
ENTRYPOINT ["java", "-jar", "app.jar"]
