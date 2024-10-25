#!/usr/env/bin bash
# #########################
# add schemas to registry #
# #########################

# add schemas
jq '. | {schema: tojson}'  /tmp/schema.avsc | curl -X POST --location 'http://schema-registry:28081/subjects/abonne-event-value/versions' --header 'Content-Type: application/vnd.schemaregistry.v1+json'  --data @-