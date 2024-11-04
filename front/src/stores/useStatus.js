import { defineStore } from "pinia";

export const useStatus = defineStore("status", {
    state: () => ({
        status: false,
    }),
    actions: {
        setStatus(status) {
            this.status = status;
        },
    },
});

