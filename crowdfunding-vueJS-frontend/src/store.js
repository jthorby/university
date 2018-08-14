import Vuex from 'vuex'
import Vue from 'vue'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    loggedIn: false,
    username: '',
    userid: -1,
    token: '',
    URL: 'http://csse-s365.canterbury.ac.nz:4978/api/v2/'
  },

  getters: {
    isLoggedIn(state) {
      return state.loggedIn
    },
    getURL(state) {
      return state.URL
    },

    getUsername(state) {
      return state.username
    },

    getUserid(state) {
      return state.userid
    },

    getToken(state) {
      return state.token
    },

    getAuthHeader(state) {
      return  { headers : {
          'X-Authorization' : state.token
        }
      }
    }
  },

  mutations: {
    login (state, {id, token, username}) {
      state.loggedIn = true;
      state.userid = id;
      state.token = token;
      state.username = username;
    },

    logout (state) {
      state.loggedIn = false;
      state.userid = -1;
      state.token = '';
      state.username = ''
    }
  },

  plugins: [
    createPersistedState({ storage: localStorage })
  ]
})

export default store