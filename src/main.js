import { createApp } from 'vue'
import router from './router'
import { createPinia } from 'pinia'
import App from './App.vue'

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'


const pinia = createPinia();
const app = createApp(App);

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
    },
})

app.use(pinia)
app.use(router)
app.use(vuetify)
app.mount('#app')