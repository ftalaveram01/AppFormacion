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
