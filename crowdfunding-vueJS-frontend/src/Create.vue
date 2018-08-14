<template>
  <div>
    <b-jumbotron header-level="4" style="border-radius: 0px; padding: 0px 15px 15px 15px" bg-variant="dark" text-variant="white">
      <template slot="header">
         create
      </template>
      <hr style="border-bottom: solid 1px white" class="my-4;">
    </b-jumbotron>

    <b-container>
      <b-form v-on:submit.prevent style="margin: 20%">
        <div class="loginCard" style>
          <b-form-input  class="top-input" id="titleInput"
                        type="text" v-model="project.title"
                        placeholder="title" required>
          </b-form-input>

          <b-form-textarea id="subtitleInput" class="mid-input" type="text"
                     v-model="project.subtitle"
                     placeholder="subtitle"
                     :rows="2"
                     :max-rows="2" required>
          </b-form-textarea>

          <b-form-textarea id="descriptionInput" class="mid-input" type="text"
                     v-model="project.description"
                     placeholder="description"
                     :rows="3"
                     :max-rows="3" required>
          </b-form-textarea>

          <b-form-input class="mid-input" id="targetInput"
                        type="number" v-model="project.target" required
                        placeholder="funding target $">
          </b-form-input>

          <div v-for="reward of project.rewards" class="mid-input my-form-control" style="max-width: 100%">
            <p style="font-weight:bold; padding:none;display: inline;noBreak;" class="noBreak"> ${{reward.amount / 100}} </p> <p class="noBreak" style=""> {{reward.description}} </p>
          </div>

          <div v-if="rewardFormVisible" class="mid-input my-form-control">
            <b-row style="margin: 0% 0% 0% 0%">
              <b-form-input class="noBreak" style="width: 20%; border-radius: 3px 0px 0px 3px;  border-right: none" id="rewardDescInput"
                            type="number" v-model="reward.amount"
                            placeholder="cost $">
              </b-form-input>
              <b-form-input class="noBreak" style="width: 74%; border-radius: 0px 0px 0px 0px; border-left: none" id="rewardDescInput"
                            type="text" v-model="reward.description"
                            placeholder="reward description">
              </b-form-input>

              <b-button class="noBreak" style="width: 6%; border-radius: 0px 3px 3px 0px; border-left: none" v-on:click="addReward">
                +
              </b-button>
            </b-row>

            <b-form-file class="mid-input" style="margin: 10px 0px 0px 0px" id="imageInput" v-model="image" choose-label="image (optional, .jpg/.png)" accept="image/jpeg, image/png"></b-form-file>

            <div v-if="image == null">
              no image selected
            </div>

            <div v-else>
              {{ image.name }}
            </div>
          </div>

          <b-button class="loginButton bottom-input" type="submit" variant="dark" v-on:click="onCreateSubmit">create</b-button>
          <p style="color: red" v-if="error !== ''"> {{ error }} </p>
        </div>
      </b-form>
    </b-container>
  </div>
</template>


<script>
  export default {
    data() {
      return {
        project: {
          "title": "",
          "subtitle": "",
          "description": "",
          "target": null,
          "creators": [
            {
              "id": this.$store.getters.getUserid
            }
          ],
          "rewards": [
          ]
        },

        image: null,
        rewardFormVisible: true,
        reward: {
          "amount": null,
          "description": ""
        },
        error: '',
      }
    },

    methods: {
      verifyInput() {

        if (this.project.title === '') { return false }
        if (this.project.subtitle === '') { return false }
        if (this.project.description === '') { return false }
        if (this.project.target === null || this.project.target <= 0 || isNaN(this.project.target)) { return false }

        return true
      },

      onCreateSubmit() {
        this.error = ''
        if (this.$store.getters.isLoggedIn) {
          this.project.target = parseInt(this.project.target)
          if (this.verifyInput()) {
            this.project.target *= 100
            this.$http.post(this.$store.getters.getURL + '/projects', this.project, this.$store.getters.getAuthHeader)
            .then(response => {
              let header = this.$store.getters.getAuthHeader
              if (this.image !== null) {
                header = {
                  headers : {
                      'X-Authorization' : this.$store.getters.getToken,
                      'content-type': this.image.type
                    }
                }
                this.$http.put(this.$store.getters.getURL + '/projects/' + response.body.id + '/image', this.image, header)
              }
            })
            .then(function() {
              if (this.error === '') {
                this.$router.push('/')
              }
            })
            .catch(error => {
              console.log(error)
            })
            this.project.target /= 100;
          } else {
            this.error = "required field(s) missing"
          }
        } else {
          this.error = "login to create a project"
        }
      },

      addReward() {
        let amount = parseInt(this.reward.amount)

        if (amount > 0 && this.reward.description !== '') {
          this.project.rewards.push({
            "amount" : amount * 100,
            "description" : this.reward.description
          })
          this.reward.amount = null
          this.reward.description = null
        }
      }
    }
  }
</script>

<style>
.my-form-control {
  display: block;
  width: 100%;
  padding: 0.5rem 0.75rem;
  font-size: 1rem;
  line-height: 1.25;
  color: #495057;
  background-color: #fff;
  background-image: none;
  /*background-clip: padding-box;*/
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 0px 0px 0px 0px;
  border-top: none;
  border-bottom: none;
  transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
}
</style>