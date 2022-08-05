[![Build & Test](https://github.com/vysker/cardboardcritic/actions/workflows/maven.yml/badge.svg)](https://github.com/vysker/cardboardcritic/actions/workflows/maven.yml)
[![Docker](https://github.com/vysker/cardboardcritic/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/vysker/cardboardcritic/actions/workflows/docker-publish.yml)

# Cardboard Critic

A board game critic aggregate.

## Dev

Note: use `mvnw.cmd` on Windows.

**Create a .env**, e.g. by copying `.env.example`.

**Build project** with `./mvnw package`.

**Run project** with live-reload: `./mvnw quarkusDev`.

**Build native image** with `./mvnw clean && ./mvnw package -P native`

**Push native image** with `docker push ghcr.io/vysker/cardboardcritic:latest-native` and `docker push ghcr.io/vysker/cardboardcritic:{version}-native`

## Inner workings

### Domain objects

* **Outlet** - News website that covers board games
* **Raw review** - Unprocessed review straight from the scraper, hidden on CBC
* **Review** - Represents a (human-)verified review, published on CBC

### Content generation

* **Crawler** - Finds article links for a specific outlet
* **Scraper** - Extracts raw reviews from article links for a specific outlet
* **Feed** - Orchestrates the review discovery process by:
  * iterating crawlers to ask for article links; 
  * filtering out links that have already been crawled;
  * calling the appropriate scrapers
  * storing the resulting reviews

## Hosting

* Host a machine with docker & docker-compose installed
* Run the following steps from that machine
* Git clone this repository
* Authenticate with the github package registry (ghcr.io), see [guide](
  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry#authenticating-to-the-container-registry)
* Create a .env file, see .env.example in this repository
* Run `docker compose up -d db`
* Run `docker compose up flyway`
* Run `docker compose up -d app`
* Add nginx and configure a reverse proxy, see `./nginx.conf.example`

### Updates

* Run `git pull`
* Run `docker compose up flyway`
* Run `docker compose up -d app`

### Operation

Finding and removing duplicate entries:

```postgresql
select url, count(*)
from raw_review
group by url
having count(*) > 1;
```

```postgresql
delete from raw_review rr using raw_review rr2
where rr.id < rr2.id
and rr.url = rr2.url;
```

## Roadmap

See `TODO.md`.
