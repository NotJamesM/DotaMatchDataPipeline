# Dota Data Pipeline
This project connects to the Valve API to scrape for recent matches. It will then export the data for data modelling.

## Requirements

* Valve API Key
* Java 18

## How to run

1. Download the latest release and
2. Place your Valve API key in: `config/datapipeline.properties` -> `valve.api.key=<your_key_here>`.
3. Run `java -jar -Dproperties.path=<path_to_your_properties> dota-draft-scraper-<VERSION>.jar`
