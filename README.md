[![Build & Test](https://github.com/vysker/cardboardcritic/actions/workflows/maven.yml/badge.svg)](https://github.com/vysker/cardboardcritic/actions/workflows/maven.yml)
[![Docker](https://github.com/vysker/cardboardcritic/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/vysker/cardboardcritic/actions/workflows/docker-publish.yml)

# Cardboard Critic

A board game critic aggregate.

## Dev

**Create a .env**, e.g. by copying `.env.example`.

**Build project** with `./mvnw package` (`mvwn.cmd package` on Windows).

**Run project** with live-reload: `./mvnw quarkusDev`.

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

## Roadmap

See `TODO.md`.
