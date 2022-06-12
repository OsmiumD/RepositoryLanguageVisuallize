<template>
  <nav>
    <router-link to="/">Dashboard</router-link>
    <router-link to="/language">Language</router-link>
    <router-link to="/timeline">Timeline</router-link>
    <router-link to="/list">List</router-link>
  </nav>
  <div class="root-content">
    <router-view/>
  </div>
</template>

<script setup>
import {ref} from "vue";
const contentWidth = 1200
const sidePadding = ref('40px')

// let pageHeight = ''
function __onWindowResize() {
  if (document.body.clientWidth > contentWidth) {
    sidePadding.value = `${Math.max((document.body.clientWidth - contentWidth) / 2, 40)}px`
  } else {
    sidePadding.value = '40px'
  }
}

window.onresize = __onWindowResize
__onWindowResize()
</script>

<style>
@font-face {
  font-family: '_Gilroy Medium';
  src: url('assets/Gilroy-Medium.otf') format('opentype');
  font-weight: normal;
}

@font-face {
  font-family: '_Gilroy Bold';
  src: url('assets/Gilroy-Bold.otf') format('opentype');
  font-weight: bold;
}

@font-face {
  font-family: '_JetBrains Mono Bold';
  src: url('assets/JetBrainsMono-Bold.ttf') format('truetype');
  font-weight: bold;
}

@font-face {
  font-family: '_JetBrains Mono Bold';
  src: url('assets/JetBrainsMono-Bold.ttf') format('truetype');
  font-weight: bold;
}

@font-face {
  font-family: '_Continuum Medium';
  src: url('assets/continuum_medium.ttf') format('truetype');
  font-weight: normal;
}

@font-face {
  font-family: '_DFHSMaruGothic W4 Reform';
  src: url('assets/DFHSMaruGothic_W4_reform.ttf') format('truetype');
  font-weight: normal;
}

/*noinspection ALL*/
#app {
  font-family: '_Gilroy Medium', '_Gilroy Bold';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.card-info {
  display: flex;
  float: right;
  color: gray;
  user-select: none;
  transition: all 0.2s linear;
}

.card-info:hover {
  color: black;
}

nav {
  text-align: center;
  height: auto;
  width: 100%;
  margin-left: 0;
  margin-right: 0;
  margin-top: 0;
  padding: 35px 0;
  border-width: 0 0 1px 0;
  border-style: solid;
  border-color: rgba(169, 169, 169, 0.85);
  background-color: white;
  box-shadow: darkgrey 0 3px 20px 0;
  user-select: none;
}

nav ul {
  display: inline-block;
  list-style-type: none;
}

nav a {
  align-self: center;
  font-weight: normal;
  padding: 15px 30px;
  margin: 0 10px;
  color: rgb(179, 184, 194);
  border-radius: 90px;
  text-decoration-line: none;
  transition: all 0.2s ease-in-out;
}

nav a:hover {
  color: rgb(255, 255, 255);
  background-color: rgb(199, 204, 213);
}

/*noinspection CssUnusedSymbol*/
nav .router-link-active:hover {
  background-color: rgb(20, 85, 254);
}

/*noinspection CssUnusedSymbol*/
nav .router-link-active {
  font-weight: bold;
  color: white;
  background-color: rgb(20, 85, 254);
  background-clip: padding-box;
  border-radius: 90px;
  filter: drop-shadow(2px 2px 15px rgba(0, 0, 0, 0.3));
  transition: all 0.2s ease-in-out;
}

.root-content {
  padding-left: v-bind(sidePadding);
  padding-right: v-bind(sidePadding);
}

code {
  font-family: '_JetBrains Mono Bold', monospace;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: rgb(20, 85, 254);
  background-color: rgb(144, 202, 249);
  padding: 5px;
  border-width: 1px;
  border-style: solid;
  border-color: #1455fe;
  border-radius: 6px;
}

a {
  color: rgb(20, 114, 254);
  text-decoration-line: underline;
  text-decoration-style: solid;
  text-decoration-color: rgb(144, 202, 249);
  text-decoration-thickness: 2px;
}
</style>
