# OdiRoute

## Requisitos previos

Antes de comenzar, asegurate de tner lo siguiente instalado en tu maquina

- **pnpm**: Gestor de paquetes.
    ```bash
      npm install -g pnpm
    ```
- Biome: Herramienta para el formato y linting de codigo
    ```bash
      pnpm add --save-dev --save-exact @biomejs/biome
    ```   
    No olvidar instalar la extension de vsCode. Se recomienda activar en settings, formatOnSave.
    Asimismo en el json de vscode colocar las siguientes lineas al final.
    ```bash
      "editor.codeActionsOnSave": {
        "source.organizeImports.biome": "explicit"
      },
      "[javascript]": {
        "editor.defaultFormatter": "biomejs.biome"
      },
      "[javascriptreact]": {
        "editor.defaultFormatter": "biomejs.biome"
      },
      "[json]": {
        "editor.defaultFormatter": "biomejs.biome"
      },
    ```
- Git: Para clonar el repositorio
- Un navegador compatible con ESModules, como Chrome o Firefox
- Node.js
  Para comprobar que lo tienes usas `node -v`
  Si no lo esta, ir a la pagina web y descargalo. 

## Instalacion

1. Clonar el repositorio
   ```bash
   git clone https://github.com/vvisalot/2B-ProSoft.git
   ```
2. Navegar hasta el directorio del proyecto

   ```bash
   cd odiroute
   ```

3. Instalar dependencias
   ```bash
   pnpm install
   ```
4. Iniciar el servidor de desarrollo
   ```bash
   pnpm run dev
   ```
5. Formatear codigo con Biome
   ```bash
   pnpm biome format
   pnpm biome lint
   ```


## Estructura del Proyecto

- src/: Contiene el código fuente de la aplicación.
- public/: Archivos públicos estáticos, como imágenes y el archivo index.html.
- vite.config.js: Configuración de Vite para gestionar el entorno de desarrollo y compilación.
- package.json: Scripts y dependencias del proyecto.
