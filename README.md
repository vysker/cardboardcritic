# Cardboard Critic

A board game critic aggregate.

## Dev

**Build project on Windows** with `.\gradlew.bat`.

**Build project on Linux** with `./gradlew`.

**Run project** with live-reload: `gradlew quarkusDev`.

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

**Phase 1 - Foundation**

* Crawl 5 outlets
* Daily, automated crawling
* Focus on new releases; not historical data
* Recent reviews page

**Phase 2 - Shippable**

* Review edit form
* Raw review approval form
* Embed video reviews
* Frontpage

**Phase 3 - Release**

* Get domain
* Deploy on VPS
* Post on social media

**Phase 4 - Beyond**

* Search functionality
* Self-submission forms for critics
* Improve visual design
* Crawl 20 outlets
* Outlet & critic page; master/detail
* Browsing, filtering, sorting, top 10
* Daily caching
* a11y
* i18n
