#!/bin/bash

BASE_URL="http://localhost:8080/send-message"

for i in {0..9}; do
    MESSAGE="Message $i"
    JSON_DATA="{\"message\": \"$MESSAGE\"}"
    curl -X POST -H "Content-Type: application/json" -d "$JSON_DATA" "$BASE_URL"
    echo ""  # Додає порожній рядок для кращого вигляду в консолі
done
