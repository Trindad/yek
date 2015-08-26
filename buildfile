repositories.remote << 'http://repo1.maven.org/maven2'

notify = lambda do
	
end

define "yek" do
  project.version = "0.0.1"
  package :jar
  run.using :main => "Main"
end
