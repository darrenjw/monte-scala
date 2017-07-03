# Makefile

FORCE:
	sbt test package

docs: FORCE
	sbt doc
	cp -r target/scala-2.12/api/* docs/api/
	git add docs/api

edit:
	emacs build.sbt README.md Makefile src/test/scala/*.scala src/main/scala/montescala/*.scala &

commit:
	git commit -a
	git push
	make update

update:
	git pull
	git log | less 


# eof
