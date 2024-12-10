import react from "@vitejs/plugin-react-swc";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [react()],
        server: {
           host: '1inf54-981-2b.inf.pucp.edu.pe', // Your desired domain
           port: 5176, // You can specify the port if needed
        },
});
