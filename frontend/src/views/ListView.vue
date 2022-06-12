<template>
  <div class="home">
    <div class="select-top" style="padding-bottom: 20px">
      <div class="select-item">
        <div class="status-item-name">Search</div>
        <el-input v-model="search" class="m-2" placeholder="" size="large" @change="searchChange">
        </el-input>
      </div>
      <div class="select-item">
        <div class="status-item-name">Language</div>
        <el-select v-model="language" class="m-2" placeholder="Select" size="large" @change="searchChange">
          <el-option
              v-for="item in languages"
              :key="item"
              :label="item"
              :value="item"
          />
        </el-select>
      </div>
      <div class="select-item" style="width: 370px">
        <div class="status-item-name">Time Range</div>
        <el-date-picker
            v-model="dateLimit"
            type="monthrange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
            @change="searchChange"
        />
      </div>
    </div>
    <el-table :data="tableData" stripe style="width: 100%" @cell-click="cellClick">
      <el-table-column prop="create_time" label="DateCreate" width="180"/>
      <el-table-column prop="name" label="Name" width="400"/>
      <el-table-column prop="main_language" label="Language"/>
      <el-table-column prop="stars" label="Stars"/>
      <el-table-column prop="forks" label="Forks"/>
    </el-table>
    <el-pagination background layout="prev, pager, next, jumper" :page-size="15" :total="total"
                   v-model:currentPage="curPage"
                   @current-change="pageChange"/>
  </div>
</template>
<script setup>
import {onMounted, ref} from "vue"

const curPage = ref(1)
const total = ref(1000)
const tableData = ref([])
const language = ref('All')
const search = ref('')
const dateLimit = ref('')
let languages = ['Java', 'Kotlin', 'Scala', 'Groovy', 'All']

function cellClick(row) {
  console.log(row.id)
}

onMounted(() => {
  pageChange()
})

function searchChange(){
  curPage.value=1;
  pageChange()
}

function pageChange() {
  fetch(`http://127.0.0.1:8081/repos/search?page=${curPage.value}&pageSize=15`
      + `&timeStart=${dateLimit.value === '' ? 0 : dateLimit.value[0].valueOf()}`
      + `&timeEnd=${dateLimit.value === '' ? 1654066500000 : dateLimit.value[1].valueOf()}`
      + `&language=${language.value}&search=${search.value}`)
      .then(r => r.json())
      .then(r => {
        total.value = r['total']
        tableData.value = r['data']
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