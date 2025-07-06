# Asset File Documentation
File extention: .asset
<br/>
Must have a png file with the same name alongside it

## asset types
There are different types of assets:
- "texture": a simple 2d Texture
- "spritesheet": a sheet of Textures
- "animation" a sheet of Textures for an Animation
- "tileset" a 47-tile tileset
- "font" a bitmap font
- "save" a savegame

Example:
```
type = "texture"
```

## sprite section
controlls the dimensions of a single sprite
<br/>
not needed for texture

Example:
```
[sprite]
width = 32
height = 32
```

## spritesheet section
amount of sprites in the spritesheet
<br/>
only needed for animation, spritesheet or font

Example:
```
[spritesheet]
count = 50
```

## animation section
duration of a single animation frame
<br/>
only needed for animation

Example:
```
[animation]
frameDuration = 0.03
```
