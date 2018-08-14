<template>
  <div id="app" >
    <div style="max-height: 50px">
      <b-navbar toggleable="md" type="dark" variant="dark">
        <router-link :to="{ name: 'home' }">
          <b-navbar-brand class="mytab"> discover </b-navbar-brand>
        </router-link>

        <div v-if="this.$store.getters.isLoggedIn">
          <router-link :to="{ name: 'create' }">
            <b-navbar-brand class="mytab"> create </b-navbar-brand>
          </router-link>

          <router-link :to="{ name: 'creating', params: {creator: $store.getters.getUserid} }">
            <b-navbar-brand class="mytab"> my projects </b-navbar-brand>
          </router-link>

          <router-link :to="{ name: 'backing', params: {backer: $store.getters.getUserid} }">
            <b-navbar-brand class="mytab"> projects I'm backing </b-navbar-brand>
          </router-link>

          <router-link :to="{ name: 'home' }">
            <b-navbar-brand class="mytab" v-on:click="logout"> logout </b-navbar-brand>
          </router-link>

        </div>

        <div v-else>
          <router-link :to="{ name: 'login' }">
            <b-navbar-brand class="mytab"> login </b-navbar-brand>
          </router-link>
        </div>

        <router-link v-if="!$store.getters.isLoggedIn" :to="{ name: 'register' }">
          <b-navbar-brand class="mytab"> register </b-navbar-brand>
        </router-link>
      </b-navbar>
    </div>

    <router-view :key="this.$route.fullPath"></router-view>

    <footer class="footer-basic-centered">

			<p class="footer-company-motto">go go? no go.</p>

      <router-link :to="{ name: 'home' }">
        <img class="footer-links logo" src="./indie.png">
        </img>
      </router-link>
		</footer>
  </div>
</template>

<script src="../node_modules/vue/dist/vue.js"></script>
<script>
export default {
  name: 'app',
  data () {
    return {
    }
  },

  mounted : function() {
    this.getProjects()
  },

  methods : {
    logout() {
      console.log(this.$store.getters.getAuthHeader)
      this.$http.post(this.$store.getters.getURL + 'users/logout', null,this.$store.getters.getAuthHeader)
      .then(response => {
        console.log(response)
        this.$store.commit('logout')
      }).catch(error => {
        console.log(error)
      })
    },

    getProjects: function() {
      this.$http.get(this.$store.getters.getURL + 'projects/')
        .then(function(response) {
          this.projects = response.data
        }, function(error) {
          this.error = error
          this.errorFlag = true
        })
    },
  }
}
</script>


<style>
.logo {
  width: 150px;
}

.loginContainer {
  border-radius: 7px;
  display: block;
  max-width: 550px;
  margin: auto;
  overflow: hidden;
  padding: 2em;
}

.mytab {
  border-bottom: 1px solid transparent;
}

.mytab:hover {
  border-bottom: solid 1px white;
}

.loginButton {
  width: 100%;
}

.loginCard {
  margin: 10px;
}

.top-input {
  border-radius: 7px 7px 0px 0px;
  border-bottom: none;
}

.mid-input {
  border-radius: 0px 0px 0px 0px;
  border-top: none;
  border-bottom: none;
}

.bottom-input {
  border-top: none;
  border-radius: 0px 0px 7px 7px;
}

.footer-basic-centered{
	background-color: #292c2f;
	box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.12);
	box-sizing: border-box;
	width: 100%;
	text-align: center;
	font: normal 18px sans-serif;
  max-height: 200px;
  margin-top: 50px;
	padding: 45px;
}

.footer-basic-centered .footer-company-motto{
	color:  #8d9093;
	font-size: 24px;
	margin: 0;
}

.footer-basic-centered .footer-company-name{
	color:  #8f9296;
	font-size: 14px;
	margin: 0;
}

.footer-basic-centered .footer-links{
	list-style: none;
	font-weight: bold;
	color:  #ffffff;
	padding: 35px 0 23px;
	margin: 0;
}

.footer-basic-centered .footer-links a {
	display:inline-block;
	text-decoration: none;
	color: inherit;
}
</style>
