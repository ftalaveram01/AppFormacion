$usuario = $env:USERNAME

$rutaProyecto = "C:\Users\$usuario\eclipse-workspace\AppFormacion\gestionformacion"
$rutaApiGateway = "C:\Users\$usuario\eclipse-workspace\AppFormacion\api-gateway"
$rutaConvocatoria = "$rutaProyecto\convocatoria-module"
$rutaCourse = "$rutaProyecto\course-module"
$rutaEurekaServer = "$rutaProyecto\eureka-server"
$rutaLogin = "$rutaProyecto\login-module"
$rutaRegister = "$rutaProyecto\register-module"
$rutaRol = "$rutaProyecto\rol-module"
$rutaUsuario = "$rutaProyecto\usuario-module"

mvn clean package -pl '!main-module' -DskipTests

Set-Location -Path $rutaApiGateway
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
        exit 1
    }
}

BuildAndVerifyImage -path $rutaConvocatoria -imageName "convocatoria"
BuildAndVerifyImage -path $rutaCourse -imageName "course"
BuildAndVerifyImage -path $rutaEurekaServer -imageName "eureka"
BuildAndVerifyImage -path $rutaLogin -imageName "login"
BuildAndVerifyImage -path $rutaRegister -imageName "register"
BuildAndVerifyImage -path $rutaRol -imageName "rol"
BuildAndVerifyImage -path $rutaUsuario -imageName "usuario"
BuildAndVerifyImage -path $rutaApiGateway -imageName "api-gateway"

Set-Location -Path $rutaProyecto
docker-compose up
