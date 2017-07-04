# http-session-caching
Sample HTTP Session caching

# Build

```
./mvnw clean package
```

# How to check sessions are stored

1. View the index page of the app. As you refresh you will see the hit counter increase while the session id remain the same.

1. Restage the app and you'll see the hit counter continue to increase and session id is still the same.
