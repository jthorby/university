

<template>
  <div>
    <b-jumbotron header-level="4" style="border-radius: 0px; padding: 0px 15px 15px 15px" bg-variant="dark" text-variant="white">
      <template slot="header">
         login
      </template>
      <hr style="border-bottom: solid 1px white" class="my-4;">
    </b-jumbotron>

    <div style="margin: 16%">
      <b-container class="loginContainer">
        <b-form v-on:submit.prevent>
          <div class="loginCard">
            <b-form-input  class="top-input" id="loginEmailInput"
                          type="text" v-model="form.user"
                          placeholder="Enter username or email address" required>
            </b-form-input>

            <b-form-input class="mid-input" id="loginPasswordInput"
                          type="password" v-model="form.password" required
                          placeholder="Enter password">
            </b-form-input>

            <b-button class="loginButton bottom-input" type="submit" variant="dark" v-on:click="onloginSubmit">login</b-button>
          </div>
        <div v-if="has_error"> Ensure data entered is correct </div>
      </b-form>
    </b-container>
    </div>
  </div>
</template>


<script>
  export default {
    data() {
      return {
        has_error: false,

        form: {
          user : "",
          email : "",
          password : ""
        },
      }
    },

    methods: {
      onloginSubmit() {
        this.has_error = false
        if (this.form.password !== '') {
          this.$http.post(this.$store.getters.getURL + 'users/login?username=' + this.form.user +
                          '&email=' + this.form.user +
                          '&password=' + this.form.password)
          .then(response2 => {
            this.$store.commit('login', {id: response2.body.id, token: response2.body.token, username: this.form.username})
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

<style>

</style>

