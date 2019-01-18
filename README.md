[![Build Status](https://travis-ci.org/bvn13/imdb-spider.svg?branch=master)](https://travis-ci.org/bvn13/imdb-spider)


# IMDB-SPIDER

**IMDB-spider written in Java**

The purpose of this project is to parse IMDB site into Java objects to be easy used when it needs.

Take a look at simple description of the [Core](core/README.md) part. It describes all the logic of parsing. 

The [Runner](runner/README.md) part aims to implement different use cases of searching movie info from CLI.

## Dependencies

- **Java 11** - tested
- JSoup
- com.fasterxml.jackson
- JUnit 4

## Plans and dones

- `[+]` - implemented already
- `[*]` - not implemented fully yet, in development
- `[ ]` - not implemented yet, in plans

```

[*] Movies
  [+] Title
  [+] Original title
  [+] Year
  [+] Poster
  [+] Storyline
  [+] Random tagline
  [+] Genres
  [+] Certificate
  [+] Official sites
  [+] Countries
  [+] Languages
  [+] Release date
  [+] Budget
  [+] Cumulative worldwide gross
  [+] Runtime
  [+] Sound mixes
  [+] Color
  [+] Aspect ratio
  [+] Taglines
  [+] AKAs

[ ] Persons

```
