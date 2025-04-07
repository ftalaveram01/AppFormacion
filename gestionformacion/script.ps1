$usuario = $env:USERNAME

$rutaProyecto = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion"
#$rutaApiGateway = "C:\Users\$usuario\PROYECTOS\AppFormacion\api-gateway"
$rutaConvocatoria = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\convocatoria-module"
$rutaCourse = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\course-module"
$rutaEurekaServer = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\eureka-server"
$rutaLogin = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\login-module"
$rutaRegister = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\register-module"
$rutaRol = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\rol-module"
$rutaUsuario = "C:\Users\$usuario\PROYECTOS\AppFormacion\gestionformacion\usuario-module"

mvn clean package -DskipTests

function BuildAndVerifyImage {
    param (
        [string]$path,
        [string]$imageName
    )
    
    Set-Location -Path $path
    docker build -t $imageName .
    
    $imageExists = docker images -q $imageName
    if ($imageExists) {
        Write-Host "La imagen de $imageName ha sido creada"
    } else {
        Write-Host "Error al crear la imagen de $imageName"
    }
}

BuildAndVerifyImage -path $rutaConvocatoria -imageName "convocatoria"
BuildAndVerifyImage -path $rutaCourse -imageName "course"
BuildAndVerifyImage -path $rutaEurekaServer -imageName "eureka"
BuildAndVerifyImage -path $rutaLogin -imageName "login"
BuildAndVerifyImage -path $rutaRegister -imageName "register"
BuildAndVerifyImage -path $rutaRol -imageName "rol"
BuildAndVerifyImage -path $rutaUsuario -imageName "usuario"

#Set-Location -Path $rutaApiGateway
#mvn clean package -DskipTests

#BuildAndVerifyImage -path $rutaApiGateway -imageName "gateway"

Set-Location -Path $rutaProyecto
docker-compose up
