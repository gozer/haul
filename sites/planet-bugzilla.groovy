@Library('nubis') import org.mozilla.nubis.Static

def nubisStatic = new org.mozilla.nubis.Static()

node {
  stage('Prep') {
    nubisStatic.prepSite()
  }

  stage ('Build') {
    docker.image('dhartnell/mozilla-planet-builder:2.0').inside('-u 0:0 -e https_proxy=$HTTPS_PROXY -e HTTPS_PROXY -e http_proxy=$HTTP_PROXY -e HTTP_PROXY') {
      sh "/usr/local/bin/planet-prod.sh"
      sh "cp -r /data/genericrhel6/src/planet.bugzilla.org/* dst/"
    }
  }

  stage('Sync') {
     nubisStatic.syncSite()
  }

}
