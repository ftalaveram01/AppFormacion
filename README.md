# AppFormacion
# 1. Descarga
Para descargar el proyecto simplemente hay que pulsar en el botón **Code** de color verde en la página del repositorio https://github.com/ftalaveram01/AppFormacion y ahí se abrirán varias opciones, le damos a la de **Download ZIP**

Una vez descargado, descomprima el archivo donde lo prefiera.
Al descomprimirlo deberá aparecer una carpeta AppFormacion, la cual contendrá una carpeta llamada **script_bbdd**, otra llamada **gestionformacion**, y otra **frontend**.

## 2. Base de datos
### Instalación
Para la base de datos utilizamos MySQL.
Primero nos dirigimos a la página de descargas https://dev.mysql.com/downloads/installer/ seleccionamos la última versión y aparecerán dos botones de descarga, le damos a descargar el archivo más grande de unos 300-400MB (redirigirá a una página pidiendo crear una cuenta, pero le damos a **No thanks, just start my download.**) 
Una vez descargado ejecutamos el archivo y lo primero será escoger el tipo de configuración y pulsamos sobre **Custom**. Lo siguiente será seleccionar los productos a instalar, saldrán tres desplegables, primero pulsamos sobre el despegable de **MySQL Servers** y pinchamos en **MySQL Server** hasta llegar a la versión y deberá aparecer una flechita verde para seleccionarlo finalmente. Seguido de ello pulsamos sobre el despegable de **Applications** y hacemos lo mismo con **MySQL Workbench**. De tal manera que en la columna de **Products To Be installed** deberán aparecer ambos productos.
Lo siguiente será darle a **Next** y nos mostrará al ruta de instalación predeterminada, pero se puede cambiar si se desea, volvemos a pinchar en **Next** y luego en **Execute** y procederán a la instalación.
Una vez instalado pulsamos en **Next** dos veces y nos dirigirá a la configuración, seleccionamos en **Config Type** la opción **Development Computer** y el resto lo mantenemos por defecto, siendo posible cambiar el puerto si es necesario, le damos a **Next**.
Ahora tienes que elegir el método de autenticación que deseas usar. El método que aparece marcado por defecto es **más moderno y seguro** , lo dejamos así y damos a **Next**. Lo siguiente será establecer la contraseña del usuario root, puedes elegir la que desees o poner root, clicamos en **Next**. Aparecerá una pestaña para configurarlo como Servicio de Windows, dejamos todo por defecto, debería estar marcado **Configure MySQL Server as a Windows Service** y **Standard System Account**, le damos a **Next** y luego a **Execute**, se aplicará la configuración y con ello la instalación estará finalizada.

### Creación
Inicializamos MySQL Workbench, pulsamos en **Local instance** e introducimos la contraseña que asignamos a root en la instalación.
Arriba a la izquierda pulsamos en **File>Open SQL Script**, navegamos a la ruta donde tenemos el proyecto descargado y en la carpeta **script_bbdd** pulsamos en el script **gestionformacion**. Se abrirá en el Workbench el script, y justo encima del script aparecerán unos iconos, dos serán un icono de trueno, pulsamos el trueno que no tiene nada más, que debería ser el de la izquierda, si dejamos el cursor sobre el botón debe aparecer un mensaje tal que **Execute the selected portion of the script or everything, if there is no selection** pulsamos sobre el botón y a la izquierda aparecerá la pestaña **SCHEMAS** y un poco a la derecha un botón pequeño para refrescar, pulsamos sobre el botón y en la pestaña debe aparecer nuestra base de datos **gestionformacion**.

## 3. Backend
### Instalación
Nos dirigimos a https://www.eclipse.org/downloads/packages/installer a la derecha debe aparecer una columna **Download**, pulsamos en **Windows>x86_64** y nos llevará a otra pestaña donde aparecerá un botón de **Download**
Una vez descargado lo ejecutamos, seleccionamos la opción **Eclipse IDE for Java Developers** y pulsamos en **Install**, una vez instalado le damos a **Launch**

Además de Eclipse es necesario instalar un plugin dentro de Eclipse para ejecutar proyectos de Spring Boot, dentro de Eclipse arriba del todo nos dirigimos a **Help>Eclipse Marketplace**, en la pestaña **Search** buscamos **Spring Tools (aka Spring Tool Suite)** y lo instalamos, puede que durante el proceso de instalación pregunte varias veces si confías en el contenido de los siguientes autores, en la ventana **Authority** te mostrará de donde proviene y una casilla en blanco que tendrás que marcar y finalmente darle a **Trust Selected**.

### Ejecución
Dentro de Eclipse, arriba a la izquiera pulsamos en **File>Import>Existing Maven Projects** pulsamos en el botón **Browse** y navegamos al proyecto, concretamente tenemos que elegir la carpeta **gestionformacion** que es la que contiene el proyecto del backend. Al elegir la carpeta en la ventaja **Projects** deben aparecer varios ficheros que tienen que estar con la casilla marcada, por último le damos a **Finish**.
Ahora a la izquierda en la ventana de **Package Explorer** deben de aparecer **gestionformacion**, **login-module**, y **register-module**, para ejecutarlo tenemos que hacer click derecho sobre **login-module** y **register-module** y darle a **Run As>Spring Boot App**, con ello ya lo tendríamos funcionando.
#### Anotación
Importante anotar que si durante la instalación de MySQL, modificamos el puerto por defecto **3306** o la contraseña **root**, tendremós que navegar dentro del **Package Explorer** dentro de los módulos a la carpeta **src/main/resources** y abrir el application.properties. En ambos tenemos es necesario modificar los campos **spring.datasource.url** (si has modificado el puerto) y **spring.datasource.password** (si has modificado la contraseña).

## 3. Frontend
### Instalación
Nos dirigimos a la página oficial de Node.js https://nodejs.org/es y lo descargamos, lo ejecutamos y en el proceso de instalación dejamos todo por defecto. Para verificar la instalacion en la cmd **npm -v** y **node -v** deben funcionar.
Ahora, nos dirigimos a la página oficial de VSCode https://code.visualstudio.com/download y descargamos la versión de nuestro Sistema Operativo, lo ejecutamos y en el proceso de instalación dejamos todo por defecto.
Lo siguiente es abrir la cmd y ejecutar **npm install -g @angular/cli** para instalar Angular en nuestro Sistema Operativo. Para verificar la instalación en la cmd el comando **ng v** debe funcionar.

### Ejecución
Abrimos VSCode y arriba a la izquierda pinchamos **File>Open Folder**, en la carpeta de nuestro proyecto nos dirigimos a **frontend>gestionUsuarios** y seleccionamos esa carpeta.
Dentro de VSCode hacemos click derecho en la carpeta gestionUsuarios y pinchamos en **Open in Integrated Terminal**, abajo se abrirá una terminal, por defecto suele ser powershell, si es así pulsamos en la flecha al lado de powershell y damos a la opción **Command Prompt**, primero ejecutamos el comando **npm install** y luego **ng serve**, accedemos a la direccion http://localhost:4200/ y ya tenemos el frontend funcionando.
#### Anotación
Hay que tener corriendo el backend también (obviamente).
