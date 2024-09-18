/** @type {import('tailwindcss').Config} */
export default {
	content: ["./src/**/*.{js,ts,jsx,tsx}", "./index.html"],
	theme: {
		extend: {
			fontSize: {
				base: "16px",
				lg: "20px",
				sm: "14px",
				xl: "24px",
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
