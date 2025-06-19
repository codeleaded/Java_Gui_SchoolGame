# Asset File Documentation
File extention: .asset
<br/>
Must have a png file with the same name alongside it

## asset type
either texture, spritesheet, animation or tileset
```
type = "texture"
```

## sprite section
controlls the dimensions of a single sprite
<br/>
not needed for texture
```
[sprite]
width = 32
height = 32
```

## spritesheet section
amount of sprites in the spritesheet
<br/>
only needed for animation or spritesheet
```
[spritesheet]
count = 50
```

## animation section
duration of a single animation frame
<br/>
only needed for animation
```
[animation]
frameDuration = 0.03
```
