# Code Battle

A gamification project.

Play RPG game while practicing code!

## Play

Download binaries from release.

Use local server and clients:

1. Open server
2. Menu Server -> Start Server
3. Open client
4. Select MultiPlayer, create a room
5. Open another client
6. Select MultiPlayer, join existed room
7. Press Start on both sides
8. Fight!

### Example code

```javascript

```

## Develop

1. Use Eclipse
2. Use Eclipse Marketplace to install Gradle IDE Pack
3. Import using Eclispe import wizard, select Gradle Project as source, then choose root folder (which contains core/, desktop/, gradle/ folders) 
4. Follow Gradle instructions to build model and select all projects to import
5. Wait for Gralde tasks to compelte

### Server

Run as Java Application:
`/CodeBattleGDX-core/src/com/codebattle/gui/server/main/MainServer.java`

### Client

Run as Java Application:
`/CodeBattleGDX-desktop/src/com/codebattle/game/desktop/DesktopLauncher.java`
