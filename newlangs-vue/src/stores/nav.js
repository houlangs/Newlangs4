import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useNavStore = defineStore(
    'nav',
    () => {
        const active = ref('home')

        return { active }
    },
    {
        //开启缓存
        persist: true
    }
)