import axios from "axios";


describe("Tests related to dishes", () => {
    const local = 'http://localhost:8080';
    const docker = 'http://localhost:80';
    const vps = 'http://146.235.31.246';
    const ip_api = vps;

    it('Should be able return all dishes available', async () => {
        const response = await axios.get(ip_api + "/api/dish/all");

        expect(response.status).equals(200, "Deveria retornar 200")

        const data = response.data.data

        console.log(data)

        const allDishIsAvailable = data.every(dish => dish.availability === 'Disponível');
        expect(allDishIsAvailable).toBe(true)
    });

    it('Should be able return all dishes available with pagination', async () => {
        const response = await axios.get(ip_api + "/api/dish/");

        expect(response.status).equals(200, "Deveria retornar 200")

        const data = response.data.data

        console.log(data)
        const content = data.content

        const allDishIsAvailable = content.every(dish => dish.availability === 'Disponível');
        expect(allDishIsAvailable).toBe(true)
    });

    it('Should be able return all dishes available by restaurant', async () => {
        const response = await axios.get(ip_api + "/api/dish/restaurant/1");

        expect(response.status).equals(200, "Deveria retornar 200")

        const data = response.data.data

        console.log(data)

        const allDishIsAvailable = data.every(dish => dish.availability === 'Disponível');
        expect(allDishIsAvailable).toBe(true)
    });



});