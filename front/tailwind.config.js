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
        "secondary": "#FFF787",
        "secondary-subtle": "#FFF787",
        "tertiary": "#1E1E1E",
        "tertiary-subtle": "#F2F2F2",
        "soft-border": "#E1E1E1",
      }
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}