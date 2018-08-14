<template>
  <div>
    <div v-if="errorFlag"></div>

    <div v-else>
      <b-container style="padding-top: 20px">
        <div>
          <b-jumbotron header-level="4" style="border-radius: 0px; padding:30px" bg-variant="dark" text-variant="white">
            <template slot="header">
              {{ project.title }}
            </template>
            <template slot="lead">
              {{ project.subtitle }}
            </template>
            <hr style="border-bottom: solid 1px white" class="my-4;">
            <div class="noBreak" v-for="creator of project.creators">
              <p class="noBreak"> By {{ creator.username }} </p>
            </div>

            <p v-if="creationDate" class="noBreak"> {{ creationDate.getDate() }}/{{ creationDate.getMonth() }}/{{ creationDate.getFullYear() }} </p>
          </b-jumbotron>

          <b-container>
            <b-row>
              <b-col>
                <div>
                  <b-img class="projectImage" style="margin-left: -13px" :src="imageuri"> </b-img> <br>
                </div>

                <div v-if="project.open">
                  <div v-if="!ownProject">
                    <b-container v-if="this.$store.getters.isLoggedIn">
                      <b-row>
                        <b-col style="margin-left: -13px; padding: 0px; width: 50%">
                          <b-form-input style=" border-radius: 0px 0px 0px 0px; width: 100%" class="noBreak" v-model="pledgeData.amount"
                            type="number"
                            placeholder="Enter amount in $">
                          </b-form-input>
                        </b-col>

                        <b-col style="margin: 0px; padding: 0px; width: 50%">
                          <b-button v-on:click="makePledge" style="width: 89%; border-radius: 0px 0px 0px 0px">
                            pledge
                          </b-button>
                        </b-col>
                      </b-row>

                      <b-row>
                        <b-form-checkbox style="margin-left:-13px" id="checkbox"
                                             v-model="pledgeData.anonymous">
                          anonymous pledge
                        </b-form-checkbox>
                      </b-row>
                      <p style="color: red"> {{ pledgeError }} </p>
                    </b-container>

                    <div v-else style="margin-left: -13px">
                      you must be logged in to pledge to this project
                    </div>
                  </div>

                  <div v-else>
                    cannot pledge to own project
                  </div>
                </div>

                <div v-else>
                  <h3> This project has closed </h3>
                </div>
              </b-col>

              <b-col>
                <!-- <b-card> -->
                  <b-tabs text-variant="dark">
                    <br>

                    <b-tab v-if="ownProject" title="creator options">
                      <b-button v-if="project.open" v-on:click="closeProject"> close project </b-button>

                      <b-form-file class="mid-input" style="margin: 40px 0px 0px 0px" id="imageInput" v-model="image" choose-label="choose new image (.jpg/.png)" accept="image/jpeg, image/png"></b-form-file>

                      <div v-if="uploadSuccess !== ''">
                        {{ uploadSuccess }}
                      </div>

                      <!-- <div v-else> -->
                        <div v-if="image == null">
                          no image selected
                        </div>

                        <div v-else>
                          {{ image.name }}
                          <b-button class="noBreak" style="margin: 10px 0px 0px 0px" v-on:click="uploadImage"> upload </b-button>
                        </div>
                      <!-- </div> -->
                    </b-tab>

                    <b-tab title="about">
                        <div>
                          <b-progress style="border-radius: 1px" class="mt-1" :max="max">
                            <b-progress-bar style="height: 10px;" variant="danger" :value="this.project.progress.currentPledged" ></b-progress-bar>
                          </b-progress> <br>

                          <h1> ${{ this.project.progress.currentPledged / 100 }} of ${{ project.target / 100 }} raised </h1>
                          <h5> {{ project.progress.numberOfBackers }} backers are bringing this project to life </h5> <br>
                        </div>

                        <p> {{ project.description }} </p>
                    </b-tab>

                    <b-tab title="rewards">
                      <div v-for="reward of project.rewards">
                        <div style="border-bottom: solid 1px">
                          <p class="noBreak" style="font-weight: bold"> ${{ reward.amount / 100 }} </p>
                        </div>
                        <p class="noBreak"> {{ reward.description }} </p> <br> <br>
                      </div> <br>
                    </b-tab>

                    <b-tab title="recent backers">
                      <div>
                        <div v-if="!hasBackers">
                          <p> No one has backed this project yet </p>
                        </div>

                        <div v-else>
                          <div v-for="pledge of recentBackers">
                            <p v-if="pledge.username !== 'anonymous'"> {{ pledge.username }} ${{ pledge.amount / 100 }}  </p>
                          </div>

                          <div style="border-top: solid black 1px" v-if="anonAmount > 0">
                            <p> anonymously backed total ${{ anonAmount / 100 }} </p>
                          </div>
                        </div>
                      </div>
                    </b-tab>
                  </b-tabs>
                <!-- </b-card> -->
              </b-col>
            </b-row>
          </b-container>
        </div>
      </b-container>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        error: "",
        errorFlag: false,
        project: {},
        projectId: 0,
        creationDate: 0,
        hasBackers: false,
        max: 0,
        pledgeData: {
          "id": this.$store.getters.getUserid,
          "amount": null,
          "anonymous": false,
          "card": {
            "authToken": "visaaaaa"
          }
        },
        image: null,
        uploadSuccess: '',
        pledgeError: '',
        imageuri : null
      }
    },

    computed: {
      openclose: function() {
        if (this.project.open) {
          return "close project"
        } else {
          return "open project"
        }
      },


      recentBackers: function() {
        let limit = 5
        let result = []

        for (let backer of this.project.backers) {
          if (backer.username !== "anonymous" && limit > 0) {
            limit -=1
            result.push(backer)
          }
        }
        return result
      },


      anonAmount: function() {
        let amount = 0
        for (let backer of this.project.backers) {
          if (backer.username == "anonymous") {
            amount += backer.amount
          }
        }
        return amount
      },

      ownProject: function() {
        let myId = this.$store.getters.getUserid
        let mine = false

        for (let creator of this.project.creators) {
          if (creator.id === myId) {
            mine = true
            break
          }
        }

        return mine
      }
    },

    mounted: function() {
      this.getProject();
    },

    methods: {
      getProject: function() {
        this.$http.get(this.$store.getters.getURL + 'projects/' + this.$route.params.projectId)
          .then(function(response) {
            this.project = response.data
            this.creationDate = new Date(this.project.creationDate)
            this.max = this.project.target

            if (this.project.progress.numberOfBackers > 0) {
              this.hasBackers = true
            }
            this.imageuri = this.$store.getters.getURL + '/projects/' + this.project.id +'/image/' + '?time=' + Date.now()
          }, function(error) {
            this.error = error
            this.errorFlag = true
          })
        },

        makePledge: function() {
          this.pledgeError = ""
          if (this.project.open === true) {
            this.pledgeData.amount = parseInt(this.pledgeData.amount)
            if (this.pledgeData.amount > 0) {
              this.pledgeData.amount *= 100
              this.$http.post(this.$store.getters.getURL + 'projects/' + this.$route.params.projectId + '/pledge',
                              this.pledgeData,
                              this.$store.getters.getAuthHeader)
              .then(function(response) {
                this.getProject()
                this.pledgeData.amount = 0
              })
              .catch(error => {
                this.pledgeError = "pledge failed"
              })
            } else {
              this.pledgeError = "pledge must be more than $0"
            }
          }
        },

        uploadImage: function() {
          this.$http.put(this.$store.getters.getURL + '/projects/' + this.project.id + '/image', this.image,
                        {
                          headers : {
                              'X-Authorization' : this.$store.getters.getToken,
                              'content-type': this.image.type
                            }
                        })
          .then(response => {
            this.getProject()
          })
          .then(response2 => {
            // this.uploadSuccess = "image uploaded"
          })
          .catch(error => {
            this.uploadSuccess = "error uploading file"
          })
        },

        closeProject: function() {
          this.$http.put(this.$store.getters.getURL + 'projects/' + this.$route.params.projectId,
                          {
                            "open": false
                          },
                          this.$store.getters.getAuthHeader)
          .then(response => {
            this.getProject()
          })
          .catch(error => {
            console.log(error)
          })
        }
      }
  }
</script>

<style>
  .noBreak {
    display: inline;
  }

  .nav-link {
    color: #343A40;
  }

  .projectImage {
    overflow-y: hidden;
    display: block;
    width: 100%;
    max-height: 600px;
    position:relative;
    object-fit: cover;
    padding-right: 20px;
  }
</style>