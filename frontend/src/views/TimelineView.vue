<template>
  <div class="home">
    <h1>
      Repository Created
    </h1>
  </div>
  <div class="select-top">
    <div class="select-item" style="width: 370px">
      <div class="status-item-name">Time Range</div>
      <el-date-picker
          v-model="dateLimit"
          type="monthrange"
          range-separator="To"
          start-placeholder="Start date"
          end-placeholder="End date"
          @change="onChange"
      />
    </div>
    <div class="select-item">
      <div class="status-item-name">Star Limit</div>
      <el-input-number v-model="starLimit" class="select-size" size="large" :min="0" :max="9999999"
                       @change="onChange"/>
    </div>
  </div>
  <div ref="timeChart" class="basicChart">
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import * as echarts from "echarts";

const timeChart = ref(null)
const dateLimit = ref('')
const starLimit = ref(10)
let ec
onMounted(() => {
  ec = echarts.init(timeChart.value)
  ec.setOption({
    title: {
      text: 'Repository Count'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['Java', 'Kotlin', 'Scala', 'Groovy']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {

        dataView: {readOnly: false},
        saveAsImage: {}
      },
      show: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: []
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'Java',
        type: 'line',
        data: []
      },
      {
        name: 'Kotlin',
        type: 'line',
        data: []
      },
      {
        name: 'Scala',
        type: 'line',
        data: []
      },
      {
        name: 'Groovy',
        type: 'line',
        data: []
      }
    ]
  })
})

function onChange() {
  if (dateLimit.value === '')
    return
  fetch(`http://127.0.0.1:8081/repos/timeline?timeStart=${dateLimit.value[0].valueOf()}&timeEnd=${dateLimit.value[1].valueOf()}&starLimit=${starLimit.value}`)
      .then(r => r.json())
      .then(r => {
        ec.setOption({
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: r['times']
          }
        })
        let ss = ec.getOption().series
        ss[0].data = r['data'][0]
        ss[1].data = r['data'][1]
        ss[2].data = r['data'][2]
        ss[3].data = r['data'][3]
        ec.setOption({series: ss})
      })
}
</script>

<style scoped>
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

.select-top {
  text-align: center;
  width: 800px;
  margin: 0 auto;
}
</style>