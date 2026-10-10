// Ecossistema
import { createApp } from 'vue';
import { createPinia } from 'pinia';

// PrimeVue e Temas
import PrimeVue from 'primevue/config';
import ToastService from 'primevue/toastservice';
import Aura from '@primeuix/themes/aura';

// Router
import { router } from '@/router';

// Estilos Globais
import '@/assets/main.css';

// Componentes
import App from '@/App.vue';

const app = createApp(App);

// Plugins
app.use(createPinia());
app.use(router);
app.use(ToastService);
app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      darkModeSelector: '.dark-mode-disabled', // Modo claro fixo por padrão
    },
  },
});

app.mount('#app');
