const ControlesSimulacion = ({
                                 simulacionActiva,
                                 pausarSimulacion,
                                 reanudarSimulacion,
                                 acelerarSimulacion,
                                 reducirSimulacion,
                                 reiniciarSimulacion,
                                 velocidad,
                             }) => {
    return (
        <div className="mb-4">
            <button
                className="px-4 py-2 bg-blue-500 text-white rounded-lg mr-2"
                onClick={simulacionActiva ? pausarSimulacion : reanudarSimulacion}
            >
                {simulacionActiva ? "Pausar" : "Reanudar"}
            </button>

            <button
                className="px-4 py-2 bg-green-500 text-white rounded-lg mr-2"
                onClick={acelerarSimulacion}
            >
                Acelerar
            </button>

            <button
                className="px-4 py-2 bg-yellow-500 text-white rounded-lg mr-2"
                onClick={reducirSimulacion}
            >
                Reducir
            </button>

            <button
                className="px-4 py-2 bg-red-500 text-white rounded-lg mr-2"
                onClick={reiniciarSimulacion}
            >
                Reiniciar
            </button>

            <div className="mt-2 text-gray-700">
                <p>Velocidad: {velocidad}x</p>
            </div>
        </div>
    );
};

export default ControlesSimulacion;