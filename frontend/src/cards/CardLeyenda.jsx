import React from 'react';
import { MapPin, Truck } from 'lucide-react';

const LegendItem = ({ icon, label, quantity, color }) => (
  <div className="flex items-center gap-3 py-1">
    {icon}
    <span className="flex-grow">{label}</span>
    <span className="font-semibold" style={{ color }}>{quantity}</span>
  </div>
);

const CardLeyenda = () => {
    return (
        <div className="w-full bg-white shadow-sm">
            <div className="p-4">
                <div className="grid grid-cols-2 gap-4 mb-4">
                <div className="font-bold underline">Leyenda</div>
                <div className="font-bold underline">Cantidad</div>
                </div>

                <div className="space-y-2">
                {/* Ubicaciones */}
                <LegendItem
                    icon={<MapPin className="text-blue-500" size={20} />}
                    label="Almacén"
                    quantity="3"
                    color="#2196F3"
                />
                <LegendItem
                    icon={<MapPin className="text-gray-700" size={20} />}
                    label="Oficina"
                    quantity="19"
                    color="#000000"
                />

                {/* Rutas */}
                <div className="flex items-center gap-3 py-1">
                    <div className="w-6 h-1 bg-amber-900"></div>
                    <span className="flex-grow">Todas las rutas</span>
                    <span className="font-semibold text-amber-900">25</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <div className="w-6 h-1 bg-blue-500"></div>
                    <span className="flex-grow">Tramo recorrido</span>
                    <span className="font-semibold text-blue-500">10</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <div className="w-6 h-1 bg-gray-400"></div>
                    <span className="flex-grow">Tramo faltante</span>
                    <span className="font-semibold text-gray-400">8</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <div className="w-6 h-1 border-t-2 border-dashed border-red-500"></div>
                    <span className="flex-grow">Tramo con bloqueo</span>
                    <span className="font-semibold text-red-500">2</span>
                </div>

                {/* Camiones */}
                <div className="flex items-center gap-3 py-1">
                    <Truck className="text-green-500" size={20} />
                    <span className="flex-grow">Camión en ruta</span>
                    <span className="font-semibold text-green-500">15</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <Truck className="text-yellow-500" size={20} />
                    <span className="flex-grow">Camión de regreso</span>
                    <span className="font-semibold text-yellow-500">2</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <Truck className="text-red-500" size={20} />
                    <span className="flex-grow">Camión con avería</span>
                    <span className="font-semibold text-red-500">2</span>
                </div>
                <div className="flex items-center gap-3 py-1">
                    <Truck className="text-gray-500" size={20} />
                    <span className="flex-grow">Camión en almacén</span>
                    <span className="font-semibold text-gray-500">8</span>
                </div>
                </div>
            </div>
        </div>
    );
};

export default CardLeyenda;