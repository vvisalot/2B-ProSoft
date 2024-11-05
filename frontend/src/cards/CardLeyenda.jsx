import React from 'react';
import { MapPin, Truck } from 'lucide-react';

const LegendItem = ({ icon, label, quantity, color }) => (
  <div className="flex items-center gap-3 py-1">
    {icon}
    <span className="flex-grow">{label}</span>
    <span className="font-semibold" style={{ color }}>{quantity}</span>
  </div>
);

const CardLeyenda = ({ numCamiones, numRutas }) => {
    return (
        <div className="max-w-xs bg-white shadow-sm">
            <div className="p-4">
                <div className="grid grid-cols-2 gap-4 mb-4">
                <div className="font-bold underline">Leyenda</div>
                <div className="font-bold underline">Cantidad</div>
                </div>

                <div className="space-y-2">
                {/* Camiones */}
                <div className="flex items-center gap-3 py-1">
                    <Truck className="text-green-500" size={20} />
                    <span className="flex-grow">Camión en ruta</span>
                    <span className="font-semibold text-green-500">{numCamiones}</span>
                </div>
                {/* Rutas */}
                <div className="flex items-center gap-3 py-1">
                    <div className="w-6 h-1 bg-red-500"></div>
                    <span className="flex-grow">Rutas en pantala</span>
                    <span className="font-semibold text-red-500">{numRutas}</span>
                </div>

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
                    quantity="20"
                    color="#000000"
                />
                </div>
            </div>
        </div>
    );
};

export default CardLeyenda;