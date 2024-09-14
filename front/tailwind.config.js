// tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  purge: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  darkMode: true,
  theme: {
    extend: {
      fontFamily: {
        'default': ['Inter', 'sans-serif'],
      },
      colors:{
        "transparent": 'transparent',
        "current": 'currentColor',
        "primary": "#E61B1B",
        "secondary": "#FFF787",
        "tertiary": "#fff",
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}