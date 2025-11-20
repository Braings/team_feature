<template>
  <div>

  </div>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: 'mapsPage',
  data() {
    const router = useRouter();

    const goToPage = (routeName) => {
            router.push({ name: routeName });
        };

    return {
      goToPage,
    }
  }
}
</script>

<style lang="scss" scoped>
 $wGREEN-color: #4CAF50;

  .map-container {
    width: 100vw;
    height: 100vh;
    background-color: $wGREEN-color;
  }
</style>
