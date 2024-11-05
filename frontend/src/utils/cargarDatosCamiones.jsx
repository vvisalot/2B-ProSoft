export const cargarDatosCamiones = async()=> {
    try {
        const data = await import("/src/assets/data/Data.json");
        return data.default;
    } catch (e) {
        console.log("Error al cargar los datos de los camiones", e);
    }
}