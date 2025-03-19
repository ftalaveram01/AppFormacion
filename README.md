# AppFormacion 
## 1. Descarga 
Para descargar el proyecto, simplemente haz clic en el botón **Code** de color verde en la página del repositorio y selecciona la opción **Download ZIP**. Una vez descargado, descomprime el archivo en la ubicación que prefieras.

Al descomprimirlo, debería aparecer una carpeta llamada **AppFormacion**, que contendrá tres subcarpetas: **script_bbdd**, **gestionformacion** y **frontend**.

## 2. Base de datos 
### Instalación Para instalar la base de datos, sigue estos pasos:

1.  Descarga el archivo más grande con la última versión de MySQL desde la [página oficial](https://www.mysql.com/downloads/).
2.  Selecciona la opción **Custom** durante la instalación.
3.  Selecciona los productos a instalar: **MySQL Server** y **MySQL Workbench**.
4.  En la configuración selecciona **Development Computer**.
5. El resto de cosas puedes dejarla por defecto.
6.  Establece la contraseña del usuario root.

### Creación Para crear la base de datos, sigue estos pasos:

1.  Abre MySQL Workbench pulsa **Local instance** e introduce la contraseña del root.
2.  **File>Open SQL Script** y abre el script **gestionformacion** desde la carpeta **script_bbdd**.
3.  Presiona el icono del trueno y  ejecuta el script para crear la base de datos.

## 3. Backend 
### Instalación Para instalar el backend, sigue estos pasos:

1.  Descarga la última versión de Eclipse desde la [página oficial](https://www.eclipse.org/downloads/packages/installer), la opción **Windows>x86_64**.
2.  Instala el plugin **Spring Tools (aka Spring Tool Suite)** desde Eclipse en **Help>Eclipse Marketplace**.

### Ejecución Para ejecutar el backend, sigue estos pasos:

1.  En Eclipse pulsa en **File>Import>Existing Maven Projects** y importa el proyecto **gestionformacion**.
2.  Aparecerá el proyecto y varios módulos, click derecho en cada uno y **Run As>Spring Boot App** para ejecutarlos.

### Escaneo con SonarQube

1. Descargar SonarQube Community Build desde la [pagina oficial](https://www.sonarsource.com/products/sonarqube/downloads/)
2. Debes descomprimir la carpeta en una ubicación de tu elección y acceder a "CARPETADESCOMPRIMIDA\sonarqube-25.2.0.102705\bin\windows-x86-64"
3. Ejecutar como administrador el archivo "StartSonar.bat"
4. Acceder a "http://localhost:9000" que es el puerto por defecto aplicado a SonarQube e iniciar sesión -> Usuario: admin , Contraseña: admin -> Resetear contraseña
5. Pulsar en "Create Local Project"
6. Rellenar "Project display name" con el nombre del proyecto y pulsar "Next"
7. Pulsar en "Use the global setting" -> Pulsar "Create Project"
8. Pulsar Locally -> Pulsa "Generate" para crear el token (guardar para mas adelante)
9. Acceder a gestionformacion/login-module/pom.xml y dentro de properties, modificar los siguientes campos
        - <sonar.token>TOKEN</sonar.token> -> Cambiar TOKEN por el token generado en el apartado anterior
        - <sonar.projectKey>KEY</sonar.projectKey> -> Cambiar KEY por la key elegida al crear el proyecto que sera igual que el nombre por defecto
        - <sonar.projectName>NAME</sonar.projectName> -> Cambiar NAME por el nombre elegido al crear el proyecto

10. Abrir una terminal en AppFormacion\gestionformacion (carpeta raiz del proyecto) y ejecutar "mvn clean verify sonar:sonar"
11. Por último, acceder a "http://localhost:9000/projects" y acceder al proyecto creado, en el saldrán los datos del escaneo.

#### Nota Si modificaste el puerto o la contraseña durante la instalación de MySQL, debes actualizar los campos **spring.datasource.url** y **spring.datasource.password** en el archivo **application.properties**.

## 4. Frontend 
### Instalación Para instalar el frontend, sigue estos pasos:

1.  Descarga la última versión de Node.js desde la [página oficial](https://nodejs.org/es).
2.  Descarga la última versión de VSCode desde la [página oficial](https://code.visualstudio.com/download).
3.  Instala Angular CLI con el comando **npm install -g @angular/cli** desde la CMD.

### Ejecución Para ejecutar el frontend, sigue estos pasos:

1.  Pulsa en **File>Open Folder** y abre la carpeta **frontend** desde VSCode.
2.  Abre la terminal integrada y ejecuta el comando **npm install**.
3.  Ejecuta el comando **ng serve** para iniciar la aplicación.

#### Nota Debes tener el backend en ejecución para que el frontend funcione correctamente.

## 5. Docker ##


### - Instalación de WSL ###
1. Ejecute el siguiente comando: **wsl --install** asi instalaremos las caracteristicas necesarias para ejecutar WSL
2. En el proceso de instalacion del wsl se le pedira que reinicie la máquina una vez reiniciada saltara la ventana del cmd para iniciar sesión
3. Crea una cuenta de usuario y una contraseña para la distribución de Linux recién instalada

### - Instalación de Docker ###
1. Visita la [página oficial](https://www.docker.com/get-started/) de Docker Desktop y descargue la opción de Windows
2. Inicie la aplicacion y deja la opción recomendada
3. Una vez dentro del Docker inicie sesión

### Posibles ERRORES ###

- Si pone algun mensaje de **WSL 2** es porque se requiere el WSL es tan sencillo como **wsl --set-default-version 2**
- En otras ocasiones podria ser simplemente que tengas que reiniciar el **Docker Desktope**


### - Creación del fichero Dockerfile ###

- En nuestro proyecto al ser un programa multiservicio y tener distintos modulos es necesario crear un **Dockerfile** en la raiz de cada modulo
- Cada modulo tiene que tener su .jar correspondiente, para generarlo haz click derecho en la raiz del proyecto ver terminal
  e introduces este comando **mvn clean package** o **mvn clean package -DskipTests** (Omitira los test si es que te fallan)
- Empezamos a rellenar los Dockerfile con esta información:
          1- Versión de **JDK**
          2- Dirección del .jar
          3- Un Entrypoint que ejecutara el .jar
          En nuestro caso la version del JDK es **openjdk:17-jdk-alpine** asi que lo aplicamos con el comando FOR [nombre del JDK]
          Continuamos con la direcion del .jar, generalmente se encuentra en el target y como no sabemos si en algun momento en alguna version del programa se actualiza el nombre del .jar
          lo cambiaremos por un *, se deberia de ver algo asi .tarjet/*.jar [apodo].jar, lo aplicaremos con el comando COPY target/*.jar nombre.jar
          Por ultimo pondremos el Entrypoint que se deberia de ver asi: [Entrypoint ["java", "-jar", "nombre.jar"]]

### - Creación del fichero Docker-compose ###

- Una vez finalizado los ficheros o fichero **Dockerfile** es hora de crear las imagenes de los modulos, iremos modulo por modulo creando sus respectivas imagenes 
  con el comando [docker build -t "nombre-adecuado" .]

- Una vez finalizado esto iremos a la creacion del fichero, lo situaremos en la raiz del proyecto esta vez con el nombre "docker-compose.yml"
- Este fichero contendra una version (no es obligatoria), unos servicios (seran los modulos que sean necesario meter), y por ultimo la base de dato y la configuración de la red y el volumen
  1. La versión en nuestro caso no la hemos añadido pero si tu la deseas añadir añade la mas reciente [version "Numero de la version"]
  2. Lo siguiente seran los servicios, estos son los modulos cada atributo que tiene es...
             [nombre-imagen]: Es el nombre del servicio
             [image: nombre de la imagen] Le indica la imagen de Docker que se usará para este servicio
             [container_name: nombre-contenedor] Es el nombre que tendra el contenedor en el **Docker Desktop**
             [ports:] Se encarga de exponer los puertos del contenedor y mapear los puertos del host
             [networks:] Sen encarga de definir la red en la que estara conectado el contenedor
             [volumes: ] Monta un volumen para compratir archivos entre el host y el contenedor en nuestro caso esta dirección lleva a nuestro modulo core
             [depends_on:] Declara que este servicio no se levantara hasta que no se levante la dependencia en nuestro caso pusimos la base de datos
 3. Lo siguiente es configurar la base de datos
             [nombre del servicio] Este sera el nombre del servicio de la base de datos
             [image: mysql] Se especifica que este servicio utilizara la imagen de Docker mysql
             [container_name:] Se define el nombre del contenedor
             [environment:] Establece variables de entorno necesarias para confiruar el contenedor de **MySQL**
             [MYSQL_ROOT_PASSWORD: contraseña-root] Defien la contraseña del usuario root
             [MYSQL_DATABASE: nombre] Crea una base de datos llamada nombre
             [MYSQL_USER: nombre] Configura un user adicional llamado admin
             [MYSQL_PASSWORD: contraseña] Se le asigna una contraseña al usuario anterior
             [ports:] Se asigna el puerto del contenedor se recomienda "3306:3306" o "2000:3306"
             [networks:] Se define el puerto del contenedor
             [volumes:] Se configura un volumen para persistir los datos de MySQL con mysql-volume:/var/lib/mysql
 4. Para configurar el network visto antes hemos utilizado el que da por defecto para esto solo define un nombre a la red
- Una vez ya configurada el docker-compose lo levantaremos con el comando [docker-compose up], si todo va bien le podras dar a la tecla v y te mostrara el contenedor en el docker

### - Posibles errores detectados al Dockerizar el proyecto ###

- Error de uso de puerto: Nosotros no conseguimos mapear los puertos por lo tanto lo pusimos en otros, apesar de ello funciona cada endpoint a la base de datos
- Error de nombre de la tabla: Por lo general tendras los nombres de las tablas en minusculas, Windwos te pide que el nombre de la tabla sea con la primera letra en mayusculas, 
 tendras que cambiar la base de datos completa unicamente el nombre de las tablas
- No da conexión con Docker: Es posible que ya tengas el **Mysql Workbench** si es asi este interceptara todas las peticiones enviadas al docker para que se ejecuten en el Workbench,
 para arreglar este error y comprobar que es asi ponemos en la cmd como administrador [netstat -nabo > (ruta en la que queramos copiar todo el comando en un fichero)], 
 esto te generara un fichero con todos los puertos en uso y sus servicio, buscamos "3306" y si vemos que es el **Workbench**
 quien lo tiene en uso nos vamos a Servico en la barra de busqueda de windows y detenemos el servicio de **MySQL80** ademas recomiendo quitar en automatico el tipo de inicio
