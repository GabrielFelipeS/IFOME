export const formatReal = (value) => {
	if (Number.isFinite(value)) {
		return value.toLocaleString('pt-BR', {style: 'decimal', minimumFractionDigits: 2, maximumFractionDigits: 2});
	}
}