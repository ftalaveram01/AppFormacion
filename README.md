# AppFormacion
# Descarga
Para descargar el proyecto simplemente hay que pulsar en el botón **Code** de color verde en la página del repositorio https://github.com/ftalaveram01/AppFormacion y ahí se abrirán varias opciones, le damos a la de **Download ZIP**

Una vez descargado, descomprima el archivo donde lo prefiera.
Al descomprimirlo deberá aparecer una carpeta AppFormacion, la cual contendrá una carpeta llamada "script_bbdd", otra llamada "gestionformacion", y otra "frontend".

## Base de datos
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
