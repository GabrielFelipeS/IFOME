// tailwind.config.js
module.exports = {
  purge: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  darkMode: true,
  theme: {
    extend: {
      fontFamily: {
        'default': ['Inter', 'sans-serif'],
      },
    },
    colors:{
      "primary": "#E61B1B",
      "secondary": "#FFF787",
    }
  },
  variants: {
    extend: {},
  },
  plugins: [],
}