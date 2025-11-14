<template>
  <div>

  </div>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: 'signPage',
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

</style>
