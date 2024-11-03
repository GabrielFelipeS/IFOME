export const getImage = (imgName) => {
	return import.meta.env.VITE_API_URL + 'image/' + imgName;
}
