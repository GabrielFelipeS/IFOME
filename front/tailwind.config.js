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
        "primary-subtle": "#FF7575",
        "primary-light": "#f38d8d",
        "primary-dark": "#b81616",
        "primary-darker": "#730e0e",
        "secondary": "#FFED00",
        "secondary-subtle": "#fff133",
        "secondary-light": "#fff680",
        "secondary-dark": "#ccbe00",
        "secondary-darker": "#807700",
        "tertiary": "#1E1E1E",
        "tertiary-subtle": "#4b4b4b",
        "tertiary-light": "#8f8f8f",
        "soft-border": "#E1E1E1",
        "background-inputs": "#F1F1F1",
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [
    require('tailwindcss-animated'),
  ],
}