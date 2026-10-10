// Ecossistema
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

// Views das Features
import RecommendationView from '@/features/recommendation/views/RecommendationView.vue';
import PetCatalogView from '@/features/pet/views/PetCatalogView.vue';
import ShelterDashboardView from '@/features/pet/views/ShelterDashboardView.vue';
import LoginView from '@/features/auth/views/LoginView.vue';
import RegisterView from '@/features/auth/views/RegisterView.vue';
import MyAdoptionsView from '@/features/adopter/views/MyAdoptionsView.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/match',
  },
  {
    path: '/match',
    name: 'recommendation-match',
    component: RecommendationView,
  },
  {
    path: '/pets',
    name: 'pet-catalog',
    component: PetCatalogView,
  },
  {
    path: '/shelter',
    name: 'shelter-dashboard',
    component: ShelterDashboardView,
  },
  {
    path: '/login',
    name: 'auth-login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'auth-register',
    component: RegisterView,
  },
  {
    path: '/my-adoptions',
    name: 'adopter-my-adoptions',
    component: MyAdoptionsView,
  },
];

export const router = createRouter({
  history: createWebHistory(),
  routes,
});
