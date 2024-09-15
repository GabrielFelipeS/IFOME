import axios from 'axios';

const viaCep = axios.create({
    baseURL: 'https://viacep.com.br/ws/'
});

export default viaCep;