import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import Home from './Home.vue'
import Project from './Project.vue'
import Register from './Register.vue'
import Login from './Login.vue'
import VueResource from 'vue-resource'
import ScrollBar from 'vue2-scrollbar'
import Vuex from 'vuex'
import store from './store.js'
import { mapGetters } from 'vuex'
import Create from './Create.vue'

import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap/dist/css/bootstrap.css'

Vue.use(BootstrapVue)
Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(Vuex)


const routes = [
  {
    path: "/",
    name: "home",
    component: Home
  },
  {
    path: "/creator/:creator",
    name: "creating",
    component: Home
  },
  {
    path: "/backer/:backer",
    name: "backing",
    component: Home
  },
  {
    path: "/projects/:projectId",
    name: "project",
    component: Project
  },
  {
    path: "/register",
    name: "register",
    component: Register
  },
  {
    path: "/login",
    name: "login",
    component: Login
  },
  {
    path: "/create",
    name: "create",
    component: Create
  }
];

const router = new VueRouter({
  routes: routes,
  mode: 'history'
})

new Vue({
  el: '#app',
  router: router,
  store: store,
  render: h => h(App)
})
