KEYCLOAK_URL=$1
if [ $# -eq 2 ]
 then
   KEYCLOAK_URL=$2
fi
export access_token=$(\
    curl -k -X POST $KEYCLOAK_URL/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 );
STAKEHOLDER_ID_JESSICA=$(curl -k -X POST "$1/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"jbfletcher@murdershewrote.com\",\"displayName\":\"Jessica Fletcher\",\"jobFunction\":\"CEO\"}"|jq -r .id);
STAKEHOLDER_ID_EMMET=$(curl -k -X POST "$1/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"doc@greatscott.movie\",\"displayName\":\"Emmett Brown\",\"jobFunction\":\"Time traveler\"}"|jq -r .id);
STAKEHOLDER_ID_WINDUP=$(curl -k -X POST "$1/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"windup@anemail.org\",\"displayName\":\"Windup\",\"jobFunction\":\"Toolkit\"}"|jq -r .id);
curl -k -X POST "$1/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Home Banking BU $STAKEHOLDER_ID_JESSICA\",  \"description\": \"Important service to let private customer use their home banking accounts\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_JESSICA  }}"|jq .;
curl -k -X POST "$1/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Online Investments service $STAKEHOLDER_ID_WINDUP\",  \"description\": \"Corporate customers investments management\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_WINDUP  }}"|jq .;
BUSINESS_SERVICE_ID_CREDIT_CARD=$(curl -k -X POST "$1/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Credit Cards BS $STAKEHOLDER_ID_EMMET\",  \"description\": \"Internal credit card creation and management service\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_EMMET  }}"|jq -r .id);
curl -k -X POST "$1/application-inventory/application" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Credit Cards Frontend $STAKEHOLDER_ID_EMMET\",  \"description\": \"Frontend application for the credit card management service\",  \"businessService\": \"api/controls/business-service/$BUSINESS_SERVICE_ID_CREDIT_CARD\"}}"|jq .;
curl -k -X POST "$1/application-inventory/application" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Credit Cards Backend $STAKEHOLDER_ID_EMMET\",  \"description\": \"Backend application for the credit card management service\",  \"businessService\": \"api/controls/business-service/$BUSINESS_SERVICE_ID_CREDIT_CARD\"}}"|jq .;
curl -k -X POST "$1/application-inventory/application" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Credit Cards Database $STAKEHOLDER_ID_EMMET\",  \"description\": \"Database for the credit card management service\",  \"businessService\": \"api/controls/business-service/$BUSINESS_SERVICE_ID_CREDIT_CARD\"}}"|jq .;
