# OdiRoute

## Requisitos previos

Antes de comenzar, asegurate de tner lo siguiente instalado en tu maquina

- **[Bun](https://bun.sh/)**: Gestor de paquetes y entorno de ejecucion.
  - Linux & MacOS
    ```bash
    curl -fsSL https://bun.sh/install | bash
    ```
  - Windows
    ```bash
    powershell -c "irm bun.sh/install.ps1 | iex"
    ```
- Git: Para clonar el repositorio
- Un navegador compatible con ESModules, como Chrome o Firefox

## Instalacion

1. Clonar el repositorio
   ```bash
   git clone https://github.com/vvisalot/2B-OdiRoute.git
   ```
2. Navegar hasta el directorio del proyecto

   ```bash
   cd odiroute
   ```

3. Instalar dependencias
   ```bash
   bun install
   ```
4. Iniciar el servidor de desarrollo
   ```bash
   bun run dev
   ```
5. Ejecutar ESLint
   ```bash
   bun run list
   ```
6. Construir el producto para produccion:
   ```bash
   bun run build
   ```
7. Prettier para formateo de codigo
   ```bash
   bun prettier --write
   ```

## Estructura del Proyecto

- src/: Contiene el código fuente de la aplicación.
- public/: Archivos públicos estáticos, como imágenes y el archivo index.html.
- .eslint.config.js: Archivo de configuración de ESLint para mantener el código limpio.
- vite.config.js: Configuración de Vite para gestionar el entorno de desarrollo y compilación.
- package.json: Scripts y dependencias del proyecto.
