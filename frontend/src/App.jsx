import {Button, Layout } from "antd";
import {Content, Header} from "antd/es/layout/layout";
import React from "react";
import {Link, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import logo from "./assets/odipark.svg";
import Planificador from "./pages/Planificador";
import Simulador from "./pages/Simulador.jsx";
import SimuladorOriginal from "./pages/SimuladorOriginal.jsx";

const App = () => {
    return (
        <Router>
            <Header className="flex justify-between items-center p-4 mb-2 bg-gray-800 text-white">
                <div className="flex items-center">
                    <img
                        src={logo}
                        alt="OdiparPack Logo"
                        className="items-center mr-2 h-[30px] w-[30px]"/>
                    <span className="text-2xl">OdiRoute</span>
                </div>

                <nav className="flex space-x-4">
                    <Link to="/Planificador">
                        <button type="text" className="text-white hover:text-yellow-500 hover:font-bold transform transition duration-200 hover:scale-110">Planificador</button>
                    </Link>
                    <Link to="/Simulador">
                        <button type="text" className="text-white hover:text-yellow-500 hover:font-bold transform transition duration-200 hover:scale-110">Simulador</button>
                    </Link>
                </nav>
            </Header>

            <Content className="bg-white">
                    <Routes>
                        <Route path="/Planificador" element={<Planificador/>}/>
                        <Route path="/Simulador" element={<Simulador/>}/>
                        {/* Ruta por defecto */}
                        <Route path="/" element={<Simulador/>}/>
                    </Routes>
            </Content>
        </Router>
    );
};

export default App;