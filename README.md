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
