$usuario = $env:USERNAME

$rutaProyecto = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion"
$rutaApiGateway = "C:\Users\$usuario\PROYECTOS\AppFormacion\api-gateway"
$rutaConvocatoria = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\convocatoria-module"
$rutaCourse = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\course-module"
$rutaLogin = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\login-module"
$rutaRegister = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\register-module"
$rutaRol = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\rol-module"
$rutaUsuario = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\usuario-module"

mvn clean package -DskipTests

Set-Location -Path $rutaConvocatoria
docker build -t "convocatoria" .
Write-Host "La imagen de convocatoria ha sido creada"

Set-Location -Path $rutaCourse
docker build -t "course" .
Write-Host "La imagen de course ha sido creada"

Set-Location -Path $rutaLogin
docker build -t "login" .
Write-Host "La imagen de login ha sido creada"

Set-Location -Path $rutaRegister
docker build -t "register" .
Write-Host "La imagen de register ha sido creada"

Set-Location -Path $rutaRol
docker build -t "rol" .
Write-Host "La imagen de rol ha sido creada"

Set-Location -Path $rutaUsuario
docker build -t "usuario" .
Write-Host "La imagen de usuario ha sido creada"

Set-Location -Path $rutaApiGateway

mvn clean package -DskipTests

docker build -t "gateway" .
Write-Host "La imagen del gateway ha sido creada"

Set-Location -Path $rutaProyecto
docker-compose up
