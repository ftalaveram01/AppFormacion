$usuario = $env:USERNAME

$rutaProyecto = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion"
$rutaConvocatoria = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\convocatoria-module"
$rutaCourse = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\course-module"
$rutaLogin = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\login-module"
$rutaRegister = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\register-module"
$rutaRol = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\rol-module"
$rutaUsuario = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\usuario-module"


mvn clean package -DskipTests

Set-Location -Path $rutaConvocatoria
docker build -t "convocatoria-app" .
Write-Host "La imagen de convocatoria-app ha sido creada"

Set-Location -Path $rutaCourse
docker build -t "course-app" .
Write-Host "La imagen de course-app ha sido creada"

Set-Location -Path $rutaLogin
docker build -t "login-app" .
Write-Host "La imagen de login-app ha sido creada"

Set-Location -Path $rutaRegister
docker build -t "register-app" .
Write-Host "La imagen de register-app ha sido creada"

Set-Location -Path $rutaRol
docker build -t "rol-app" .
Write-Host "La imagen de rol-app ha sido creada"

Set-Location -Path $rutaUsuario
docker build -t "usuario-app" .
Write-Host "La imagen de usuario-app ha sido creada"


Set-Location -Path $rutaProyecto
docker-compose up
