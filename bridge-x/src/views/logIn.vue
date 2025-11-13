<template>
  <div>
    <div class="backbon">
      <div class="logIn-Box">
        <p class="title cc-font">B R I D G E - X</p>
        <p class="logIn">Log In</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  setup () {


    return {}
  }
}
</script>

<style lang="scss" scoped>
  .cursorPointer {
    cursor: pointer;
  }

  .cc-font {
    font-family: 'CC', sans-serif;
  }

  .backbon{
    width: 100vw;
    height: 100vh;
    background-color: #c5e7d1;
  }

  .backbon .logIn-Box{
    border-radius: 10%/15%;
    position: fixed;
    top: 10vh;
    right: 20vw;
    width: 60vw;
    height: 70vh;
    background-color: white;
  }

  .logIn-Box .title{
    font-size: 5vw;
    font-weight: bold;
    margin: 0;
    line-height: 5vh;
    position: relative;
    left: 13vw;
    top: 10vh;
  }

  .logIn-Box .logIn{
    font-size: 3vw;
    font-weight: bold;
    margin: 0;
    line-height: 5vh;
    position: relative;
    left: 45vw;
    top: 45vh;
  }
</style>
