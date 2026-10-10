<script setup lang="ts">
// PrimeVue e UI
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';

/**
 * @property {string | null} videoUrl - URL do vídeo a ser exibido.
 * @property {string} title - Título exibido no modal.
 */
type TProps = {
  videoUrl: string | null;
  title: string;
};

// Props
const props = defineProps<TProps>();

// Reativas - Model
const isVisible = defineModel<boolean>('visible', { default: false });

// Funções
function handleClose(): void {
  isVisible.value = false;
}
</script>

<template>
  <Dialog
    v-model:visible="isVisible"
    modal
    :header="props.title"
    :style="{ width: '90vw', maxWidth: '720px' }"
  >
    <div class="py-2">
      <div v-if="props.videoUrl" class="aspect-video w-full rounded-xl overflow-hidden bg-black flex items-center justify-center">
        <!-- Vídeo direto MP4 -->
        <video
          v-if="props.videoUrl.endsWith('.mp4')"
          :src="props.videoUrl"
          controls
          autoplay
          class="w-full h-full object-contain"
        >
          Seu navegador não suporta a tag de vídeo.
        </video>
        <!-- Iframe genérico (YouTube/Vimeo) -->
        <iframe
          v-else
          :src="props.videoUrl"
          class="w-full h-full border-0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
      <p v-else class="text-slate-500 text-center py-8">
        Nenhum vídeo disponível para este animal.
      </p>

      <div class="mt-4 flex justify-end">
        <Button label="Fechar" severity="secondary" @click="handleClose" />
      </div>
    </div>
  </Dialog>
</template>
