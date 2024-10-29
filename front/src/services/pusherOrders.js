import Pusher from "pusher-js";

const pusher = new Pusher('a06e37233576d7712076',{
    cluster: 'us2',
});

export default pusher;