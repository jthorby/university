<template>
  <div>

    <div>
      <div v-if="errorFlag" style="color: red">
        {{ error }}
      </div>

      <div v-else>
        <div v-if="filter" id="projects">
          <b-jumbotron header-level="4" style="border-radius: 0px; padding: 0px 15px 15px 15px" bg-variant="dark" text-variant="white">
            <template slot="header">
               {{banner}}
            </template>
            <hr style="border-bottom: solid 1px white" class="my-4;">
          </b-jumbotron>

          <div>
            <b-container class="project-container">
              <div style="margin-bottom: 25px; border-bottom: solid 1px black">
                  <input placeholder="search projects..." style="width: 100%; border: none; font-size: 17pt" v-model="query"> </input>
              </div>

              <div v-if="filteredProjects.length > 0">
                <div class="row" style="display:flex;justify-content:center;align-items:flex-end;">
                  <div v-for="project of filteredProjects" class="grid">
                    <router-link class="card-link" :to="{ name: 'project', params: {projectId: project.id } }">
                      <div class="col-sm-5">
                        <b-card class="projectCard"
                                bg-variant="dark"
                                text-variant="white"
                                img-top
                                img-alt="This project has no image or it has not loaded"
                                v-bind:img-src="$store.getters.getURL + '/projects/' + project.id +'/image/' + imageuri">
                          <p class="card_title"> {{ project.title }} </p>
                          <p class="card-subtitle"> {{ project.subtitle }} </p>
                          <p class="cardFooter" v-if="!project.open"> CLOSED </p>
                        </b-card>
                      </div>
                    </router-link>
                  </div>
                </div>

                <div class="grid" style="display:flex;justify-content:center;align-items:flex-end;">
                  <div v-if="moreProjects">
                    <button class="see-more-button" :pressed="null" v-on:click="numProjects+=6">see more</button>
                  </div>
                </div>
              </div>

              <div v-else>
                no projects to display
              </div>
            </b-container>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        error: "",
        errorFlag: false,
        projects: [],
        numProjects: 6,
        banner: 'discover',
        query: '',
        imageuri : '?time=' + Date.now(),
        reqId: 0
      }
    },

    computed: {
      filter: function() {
        return true
      },

      moreProjects: function() {
        return this.numProjects < this.projects.length
      },

      filteredProjects: function () {
        let limit = this.numProjects
        let result = []

        for (let project of this.projects) {
          if (limit > 0) {
            let title = project.title.toLowerCase()
            let subtitle = project.subtitle.toLowerCase()
            this.query = this.query.toLowerCase()
            if (title.includes(this.query) || (subtitle.includes(this.query))) {
              if ((project.open || (this.banner === 'my projects') || (this.banner === "projects I'm backing")) && ((!this.reqId) || (this.$store.getters.getUserid == this.reqId))) {
                limit -= 1
                result.push(project)
              }
            }
          }
        }
        return result
      }
    },

    mounted: function() {
      this.getProjects();
    },

    methods: {
      getProjects: function() {
        let params = this.$route.params
        let request = this.$store.getters.getURL + 'projects/'
        let key = Object.keys(params)[0]

        if (key === "backer") {
          this.banner = "projects I'm backing"
        } else if (key === "creator") {
          this.banner = "my projects"
        } else {
          this.banner = "discover"
        }

        let value = Object.values(params)[0]
        this.reqId = value
        let options = '?' + key + '=' + value

        if (key === undefined || value === undefined) {
          options = ''
        }

        this.$http.get(request + options, null, {
          headers: {
            'Cache-Control' : 'max-age=0'
          }
        })
          .then(function(response) {
            this.projects = response.data
          }, function(error) {
            this.error = error
            this.errorFlag = true
          })
      }
    }
  }
</script>

<style>
  .cardFooter {
    margin-bottom: 1rem;
    position: absolute;
    bottom: 0;
  }

  .see-more-button {
    border-radius: 5px;
    border-color: #6d7582;
    background-color: #6d7582;
    border-style: solid;
    color: white;
  }

  .see-more-button:hover {
    border-color: #17a2b8;
  }

  .center {
    margin: auto;
  }
  .grid {
    padding-bottom: 30px;
  }

  .card img {
    overflow-y: hidden;
    display: block;
    width: 100%;
    height: 275px;
    position:relative;
    object-fit: cover;
  }

  .card_title {
    font-size: 16pt;
    font-weight: bold;
  }

  .card-subtitle {
    font-size: 10pt;
  }

  .projectCard {
    width: 350px;
    height: 500px;
    border-radius: 2px;
  }

  .projectCard:hover {
    box-shadow: 4px 4px 10px 0px #17a2b8;
  }

  .projects-header {
    padding: 30px;
    text-align: center;
  }
</style>