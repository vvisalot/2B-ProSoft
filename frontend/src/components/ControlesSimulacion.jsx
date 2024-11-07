const ControlesSimulacion = ({
                                 simulacionActiva,
                                 simulacionIniciada, // Nuevo estado para verificar si ya inició
                                 pausarSimulacion,
                                 reanudarSimulacion,
                                 iniciarSimulacion, // Nueva función para iniciar la simulación
                                 pararSimulacion, // Nueva función para parar la simulación
                                 acelerarSimulacion,
                                 reducirSimulacion,
                                 velocidad,
                             }) => {
    return (
        <div className="mb-4 flex space-x-2">
            {/* Botón para reducir la velocidad */}
            <button
                className="px-4 py-2 bg-yellow-500 text-white rounded-lg"
                onClick={reducirSimulacion}
            >
                Disminuir velocidad
            </button>

            {/* Botón de Iniciar/Pausar */}
            <button
                className="px-4 py-2 bg-blue-500 text-white rounded-lg"
                onClick={simulacionIniciada ? (simulacionActiva ? pausarSimulacion : reanudarSimulacion) : iniciarSimulacion}
            >
                {simulacionIniciada ? (simulacionActiva ? "Pausar" : "Reanudar") : "Iniciar"}
            </button>

            {/* Botón de parar la simulación */}
            <button
                className="px-4 py-2 bg-red-500 text-white rounded-lg"
                onClick={pararSimulacion}
            >
                Parar
            </button>

            {/* Botón para aumentar la velocidad */}
            <button
                className="px-4 py-2 bg-green-500 text-white rounded-lg"
                onClick={acelerarSimulacion}
            >
                Aumentar velocidad
            </button>

            {/* Mostrar la velocidad actual */}
            <div className="mt-2 text-gray-700">
                <p>Velocidad: {velocidad}x</p>
            </div>
        </div>
    );
};

export default ControlesSimulacion;