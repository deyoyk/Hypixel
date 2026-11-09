# Hypixel

<small>Fork of [Swofty-Developments/HypixelRecreation](https://github.com/Swofty-Developments/HypixelRecreation).</small>

## Run

Build all jars once (needs Docker):

```bat
tools\docker\build-jars.bat
```

```bash
docker compose up --build -d
```

Build one module only:

```bat
gradlew.bat :platform:packer:shadowJar
```

Connect with Minecraft to `localhost:25565`. `docker compose ps` shows service health, `docker compose down` stops everything.
