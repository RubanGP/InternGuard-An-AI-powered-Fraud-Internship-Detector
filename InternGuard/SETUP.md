
from PIL import Image

# Create a small game sprite: 4 pixels wide, 3 pixels tall
sprite = Image.new("RGB", (4, 3), "white")

pixels = sprite.load()

# Draw a simple character using coloured pixels
for x in range(1, 3):
    pixels[x, 0] = (255, 215, 0)  # Yellow head

for x in range(4):
    pixels[x, 1] = (0, 100, 255)  # Blue body
    pixels[x, 2] = (0, 100, 255)  # Blue legs

# Scaling factors
sx = 2
sy = 3

# Calculate the new dimensions
new_width = sprite.width * sx
new_height = sprite.height * sy

# Enlarge the sprite using raster scaling
enlarged = sprite.resize(
    (new_width, new_height),
    Image.Resampling.NEAREST
)

# Display original and enlarged sprites
sprite.resize((200, 150)).show()
enlarged.show()

print("Original size:", sprite.size)
print("Enlarged size:", enlarged.size)
