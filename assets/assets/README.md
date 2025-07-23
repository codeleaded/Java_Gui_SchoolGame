# Asset File Documentation
File extention: .asset
<br/>
Must have a png file with the same name alongside it

## asset types
There are different types of assets:
- "texture": just a simple 2d Texture
- "spritesheet": a sheet of Textures
- "animation" an Animation from a sheet of Textures 
- "tileset" to declare 47-tile tileset
- "font" a bitmap font

Example:
```
type = "texture"
```

## sprite section
controlls the dimensions of the sprites
<br/>
not needed for texture and font

Example for a single size:
```
[sprite]
width = 32
height = 32
```

Example for a multiple sizes:
```
[sprite]
sprites = [
    [ x, y, w, h ],
    [ x, y, w, h ]
]
```

## spritesheet section
amount of sprites in the spritesheet
<br/>
only needed for animation and spritesheet

Example:
```
[spritesheet]
count = 50
```

## animation section
duration of a single animation frame and animation mode (Loop/Single/Bounce)
<br/>
only needed for animation

Example:
```
[animation]
frameDuration = 0.03
mode = "Loop"
```

## audio section
Randomize pitches of a sound

Example:
```
[audio]
minPitch = 0.5
maxPitch = 2.0
```
