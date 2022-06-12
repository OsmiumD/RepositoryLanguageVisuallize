<template>
  <div class="home">
    <h1>
      <font-awesome-icon icon="database"/>
      <i class="space20px"/>Database Summary
    </h1>
    <div id="status-card">
      <div class="status-item-row">
        <div class="status-item-name">Repository</div>
        <div class="status-item-value">{{ reposInfo.total }}</div>
      </div>
      <div class="status-item-row">
        <div class="status-item-name">Java</div>
        <div class="status-item-value">{{ reposInfo.java }}</div>
      </div>
      <div class="status-item-row">
        <div class="status-item-name">Kotlin</div>
        <div class="status-item-value">{{ reposInfo.kotlin }}</div>
      </div>
      <div class="status-item-row">
        <div class="status-item-name">Scala</div>
        <div class="status-item-value">{{ reposInfo.scala }}</div>
      </div>
      <div class="status-item-row">
        <div class="status-item-name">Groovy</div>
        <div class="status-item-value">{{ reposInfo.groovy }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {library} from '@fortawesome/fontawesome-svg-core'
import {faDatabase} from '@fortawesome/free-solid-svg-icons'
import 'element-plus/dist/index.css'
import {reactive} from "vue";

library.add(faDatabase);

const reposInfo = reactive({
  total: 0,
  java: 0,
  kotlin: 0,
  scala: 0,
  groovy: 0
})

fetch("http://127.0.0.1:8081/repos/summary")
    .then(r => r.json())
    .then(r => {
      reposInfo.total = r.total
      reposInfo.java = r.java
      reposInfo.kotlin = r.kotlin
      reposInfo.scala = r.scala
      reposInfo.groovy = r.groovy
    })

</script>

<style scoped>
.home {
  padding: 20px 50px;
}

.space20px {
  margin-left: 10px;
  margin-right: 10px;
}

#status-card {
  font-size: large;
  background-color: white;
  border-radius: 15px;
  border-style: none;
  width: 400px;
  height: fit-content;
  padding: 20px;
  margin-top: 40px;
  margin-left: 20px;
  margin-bottom: 40px;
}

.status-item-row {
  padding: 15px;
  width: auto;
  height: auto;
  display: flex;
  justify-content: space-between;
}

.status-item-name {
  color: gray;
  margin-right: 50px;
}

.status-item-value {
  display: flex;
  justify-content: center;
  margin-left: 100px;
  align-items: center;
  text-indent: 2px;
}
</style>