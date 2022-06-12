<template>
  <div class="home">
    <div style="text-align: center">
      <font-awesome-icon icon="angle-left" class="arrow" @click="changeTopic"/>
      <h1 class="select-topic">
        {{ topic === 0 ? "Language" : "Topic" }}
      </h1>
      <font-awesome-icon icon="angle-right" class="arrow" @click="changeTopic"/>
    </div>
    <div class="select-top">
      <div class="select-item">
        <div class="status-item-name">Language</div>
        <el-select v-model="language" class="m-2" placeholder="Select" size="large" @change="onLangSelect">
          <el-option
              v-for="item in languages"
              :key="item"
              :label="item"
              :value="item"
          />
        </el-select>
      </div>

      <div class="select-item">
        <div class="status-item-name">Aggregation</div>
        <el-select v-model="aggregationMethod" class="m-2" placeholder="Select" size="large" @change="onLangSelect">
          <el-option
              v-for="item in methods"
              :key="item"
              :label="item.text"
              :value="item.method"
          />
        </el-select>
      </div>

      <div class="select-item">
        <div class="status-item-name">Star Limit</div>
        <el-input-number v-model="starLimit" class="select-size" size="large" :min="10" :max="9999999"
                         @change="onLangSelect"/>
      </div>

      <div class="select-item">
        <div class="status-item-name">Top</div>
        <el-input-number v-model="topLimit" class="select-size" size="large" :min="1" :max="30"
                         @change="onLangSelect"/>
      </div>
    </div>


    <div ref="langChart" class="basicChart">
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import * as echarts from "echarts"
import {library} from '@fortawesome/fontawesome-svg-core'
import {faAngleRight, faAngleLeft} from '@fortawesome/free-solid-svg-icons'

library.add(faAngleRight, faAngleLeft)
let langChart = ref(null)
const language = ref('Java')
const aggregationMethod = ref(0)
const starLimit = ref(10)
const topLimit = ref(5)
const topic = ref(0)
let languages = ['Java', 'Kotlin', 'Scala', 'Groovy']
let methods = [{text: 'Addition', method: 0}, {text: 'Weighted Addition', method: 1}, {text: 'Weighted-log', method: 2}]

let ec
onMounted(() => {
  ec = echarts.init(langChart.value)
  ec.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center',
      textStyle: {
        fontSize: 15
      }
    },
    toolbox: {
      feature: {

        dataView: {readOnly: false},
        saveAsImage: {}
      },
      show: true
    },
    series: [
      {
        top: '15%',
        name: 'Language',
        type: 'pie',
        radius: '75%',
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 5,
          borderWidth: 2
        },
        label: {
          show: true
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '20',
            fontWeight: 'bold'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        labelLine: {
          show: true
        },
        data: [
          {value: -1, name: 'fetching...'}
        ]
      }
    ]
  })
  onLangSelect()
})

function onLangSelect() {
  fetch(`http://127.0.0.1:8081/repos/${topic.value === 0 ? 'language' : 'topic'}?` +
      `language=${language.value}&aggr=${aggregationMethod.value}&starLimit=${starLimit.value}&topLimit=${topLimit.value}`)
      .then(r => r.json())
      .then(r => {
        console.log(r)
        let data = []
        for (let key in r) {
          data.push({value: r[key].sum, name: r[key].language})
        }
        let ss = ec.getOption().series
        ss[0].data = data
        ec.setOption({
          series: ss
        })
      })
}

function changeTopic() {
  if (topic.value === 0)
    topic.value = 1
  else
    topic.value = 0
  onLangSelect()
}
</script>

<style scoped>

.select-top {
  text-align: center;
  width: 800px;
  margin: 0 auto;
}

.home {
  padding: 30px 50px;
  text-align: center;
}

.basicChart {
  padding: 50px 0;
  width: 1000px;
  height: 600px;
  margin: 0 auto;
}

.arrow {
  height: 30px;
  width: 30px;
  margin: 0 35px;
  display: inline-block;
  cursor: pointer;
}


.status-item-name {
  font-size: 20px;
  color: gray;
  text-align: center;
  padding: 20px 0;
}

.select-size {
  width: 222px;
  height: 40px;
}

.select-item {
  width: 222px;
  display: inline-block;
  margin: 0 40px;
}

.select-topic {
  text-align: center;
  display: inline-block;
  width: 160px;
  padding: 0 50px;
}
</style>