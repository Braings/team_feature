<template>
  <div>
    <div :class="{ 'title': true, 'flame': true}"></div>
    <router-view />
  </div>
</template>

<script>
export default {
  setup () {


    return {}
  }
}
</script>

<style>

@font-face {
  font-family: 'CC';
  src: url('/fonts/The Jamsil OTF 6 ExtraBold.otf') format('opentype');
  font-weight: normal;
  font-style: normal;
}

</style>
