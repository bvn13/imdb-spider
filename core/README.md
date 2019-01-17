# IMDB-SPIDER :: CORE

_This is the core of IMDB-Spider._

The main ideology of this project is based on Tasks and Workers that perform given tasks in parallel.

The [Task](core/src/main/java/ru/bvn13/imdbspider/spider/tasker/Task.java) contains everything need to grub any IMDB object.

But tasks and workers would be nothing without their assistants. 

They are: 

- [Extractors](src/main/java/ru/bvn13/imdbspider/spider/extractor)
- [Processors](src/main/java/ru/bvn13/imdbspider/spider/processor)
- [Composers](src/main/java/ru/bvn13/imdbspider/spider/composer)





