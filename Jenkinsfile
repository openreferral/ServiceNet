import org.benetech.build.versioning.PomXmlVersioner

dockerbuild {
	gitUrl = "git@github.com:benetech/ServiceNet.git"
	dockerFile = "src/main/docker/Dockerfile-multistage"
	dockerRepository = "814633283276.dkr.ecr.us-east-1.amazonaws.com"
	artifactName = "servicenet"
	versioner = new PomXmlVersioner()
}
