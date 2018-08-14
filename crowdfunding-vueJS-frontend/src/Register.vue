

<template>
  <div>
    <div v-if="this.$store.getters.isLoggedIn === true"> Please log out before registering an account </div>
    <div v-else>
      <b-jumbotron header-level="4" style="border-radius: 0px; padding: 0px 15px 15px 15px" bg-variant="dark" text-variant="white">
        <template slot="header">
           register
        </template>
        <hr style="border-bottom: solid 1px white" class="my-4;">
      </b-jumbotron>
      <div style="margin: 14%">
        <b-container class="loginContainer">
          <b-form v-on:submit.prevent>
            <div class="loginCard">
              <b-form-input class="top-input" id="registerEmailInput"
                            type="email" v-model="form.email" required
                            placeholder="Enter email address">
              </b-form-input>

              <b-form-input class="mid-input" id="registerUsernameInput"
                            type="text" v-model="form.username" required
                            placeholder="Enter username">
              </b-form-input>

              <b-form-input class="mid-input" id="registerPasswordInput"
                            type="password" v-model="form.password" required
                            placeholder="Enter password">
              </b-form-input>

              <b-form-input class="mid-input" id="registerLocationInput"
                            type="text"
                            placeholder="Enter location (optional)" v-model="form.location">
              </b-form-input>

              <b-button class="loginButton bottom-input" type="submit" variant="dark" v-on:click="onRegisterSubmit">Register</b-button>

              <div v-if="has_error"> Ensure data entered is correct </div>
            </div>
          </b-form>
        </b-container>
      </div>
    </div>
  </div>
</template>


<script>
  export default {
    data() {
      return {
        has_error: false,

        form: {
          username : "",
          email : "",
          password : "",
          location : ""
        },
      }
    },

    methods: {
      validateEmail() {
        let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(this.form.email);
      },

      onRegisterSubmit() {
        this.has_error = false
        if (this.form.username !== '' && this.form.password !== '' && this.validateEmail()) {
          this.$http.post(this.$store.getters.getURL + 'users/',
                          JSON.stringify(this.form)
          ).then(response => {
            this.$http.post(this.$store.getters.getURL + 'users/login?username=' +
                            this.form.username +
                            '&password=' + this.form.password)
            .then(response2 => {
              this.$store.commit('login', {id: response2.body.id, token: response2.body.token, username: this.form.username})
            })
          }).catch((error) => {
            this.has_error = true
            console.log(error)
          }).then(function() {
            if (this.has_error === false) {
              this.$router.push('/')
            }
          })
        } else {
          this.has_error = true
        }
      }
    }
  };
</script>

