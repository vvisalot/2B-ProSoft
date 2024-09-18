import { Input } from "antd";

const { Search } = Input;

const BarraBusqueda = () => {
	const handleSearch = (value) => {
			console.log("Texto de búsqueda:", value)	;

		// Aquí puedes agregar la lógica para realizar la búsqueda
	};

	return <Search placeholder="Buscar..." onSearch={handleSearch} enterButton />;
};

export default BarraBusqueda;
