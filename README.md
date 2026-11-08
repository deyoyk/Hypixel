# Hypixel

## Run

Build all jars once (needs Docker):

```bat
tools\docker\build-jars.bat
```

```bash
docker compose up --build -d
```

Connect with Minecraft to `localhost:25565`. `docker compose ps` shows service health, `docker compose down` stops everything.
