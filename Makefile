# Makefile

FORCE:
	sbt test package

docs: FORCE
	sbt doc
	cp -r target/scala-2.12/api/* docs/api/
	git add docs/api

edit:
	emacs src/main/scala/montescala/*.scala src/test/scala/*.scala build.sbt README.md &



