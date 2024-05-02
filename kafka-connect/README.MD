## Kafka Connect Debezium Driver for PostgreSQL

PostgreSQL Debezium driver is already available in this directory.

Unzip this into a Kafka plugins directory

To install the debezium drivers for any database, Download the driver from <a href="https://debezium.io/documentation/reference/stable/install.html">debezium site.</a>

1. Install these under plugins directory in the Kafka installation
2. Open the config connect-distributed.properties and add following towards the end of the file
3. plugin.path=/kafka/plugins (The directory you have downloaded the debezium driver)



