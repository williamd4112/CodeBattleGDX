# Code Battle

Software studio course final project. 

A gamification project.

Play RPG game while practicing code!

## Play

### Install

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
Lancer.skill(18, 23);
Saber.skill(10, 11);
Lancer.moveRight(1);
vs.createGameActor('Saber', 'light', 22, 24);
```

### Instructions

Players can only control their own units.
When you click a unit, you can know which team it belongs to by the color displayed on the ground, and more details are shown in the lower-left panel containing some APIs you can call.

Due to lack of time, not all APIs are displayed in the panel, you could look at the file  `/CodeBattleGDX-core/src/com/codebattle/model/gameactor/GameActorProxy.java` which contains all usable APIs.

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

## Credit

Sprites are downloaded from [The Spriters Resource](http://www.spriters-resource.com/)
