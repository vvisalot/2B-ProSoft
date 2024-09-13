/** @type {import('tailwindcss').Config} */
export default {
	content: ["./src/**/*.{js,ts,jsx,tsx}", "./index.html"],
	theme: {
		extend: {
			fontFamily: {
				sans: [
					"Inter",
					"system-ui",
					"Avenir",
					"Helvetica",
					"Arial",
					"sans-serif",
				],
			},
			colors: {
				primary: "#646cff",
				secondary: "#535bf2",
				background: "24242424",
				lightBackground: "#ffffff",
			},
		},
	},
	plugins: [],
};
