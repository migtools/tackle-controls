export access_token=$(\
    curl -k -X POST https://$1/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 );
STAKEHOLDER_ID_JESSICA=$(curl -k -X POST "https://$1/api/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"jbfletcher@murdershewrote.com\",\"displayName\":\"Jessica Fletcher\",\"jobFunction\":\"CEO\"}"|jq -r .id);
STAKEHOLDER_ID_EMMET=$(curl -k -X POST "https://$1/api/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"doc@greatscott.movie\",\"displayName\":\"Emmett Brown\",\"jobFunction\":\"Time traveler\"}"|jq -r .id);
STAKEHOLDER_ID_WINDUP=$(curl -k -X POST "https://$1/api/controls/stakeholder" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{\"email\":\"windup@anemail.org\",\"displayName\":\"Windup\",\"jobFunction\":\"Toolkit\"}"|jq -r .id);
curl -k -X POST "https://$1/api/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Home Banking BU $STAKEHOLDER_ID_JESSICA\",  \"description\": \"Important service to let private customer use their home banking accounts\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_JESSICA  }}"|jq .;
curl -k -X POST "https://$1/api/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Online Investments service $STAKEHOLDER_ID_WINDUP\",  \"description\": \"Corporate customers investments management\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_WINDUP  }}"|jq .;
curl -k -X POST "https://$1/api/controls/business-service" -H  "accept: application/json" -H  "Content-Type: application/json" -H "Authorization: Bearer "$access_token -d "{  \"name\": \"Credit Cards BS $STAKEHOLDER_ID_EMMET\",  \"description\": \"Internal credit card creation and management service\",  \"owner\": {    \"id\": $STAKEHOLDER_ID_EMMET  }}"|jq .;
