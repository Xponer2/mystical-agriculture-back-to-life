# Mystical Agriculture: Back to Life

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](LICENSE)
[![Minecraft 1.12.2](https://img.shields.io/badge/Minecraft-1.12.2-green.svg?style=flat-square)](#)
![Made with AI (Claude)](https://img.shields.io/badge/made%20with-AI%20(Claude)-8A2BE2?style=flat-square)

An unofficial port of **Mystical Agriculture 8.0** (Minecraft 1.21.1) to **Minecraft 1.12.2** (Forge): the same
items, blocks, ids, tags, recipes, textures and features as the modern Mystical Agriculture, on 1.12.2.

Not made or supported by BlakeBr0, the author of [Mystical Agriculture](https://github.com/BlakeBr0/MysticalAgriculture).
Please report problems with this port here.

**Made with AI:** this port is made entirely with AI (Claude), from the code changes to the in-game testing.

**Work in progress:** not playable yet.

## Requirements

- Minecraft 1.12.2, Forge 14.23.5.2847 or newer.
- [Translator](https://github.com/Xponer2/translator): the library that provides the modern Minecraft API on 1.12.2.
- [Cucumber Library: Back to Life](https://github.com/Xponer2/cucumber-back-to-life).
- Can't be used together with the original Mystical Agriculture or Cucumber for 1.12.2, or the mods that need them.

Content that Minecraft 1.12.2 doesn't have (copper, netherite, sculk, turtles, bees, ...) has no recipes; recipes
that give such content give something similar where 1.12.2 has it (tuff gives andesite, warped stems give spruce
logs).

## Building

Clone the two libraries next to this repository, then build here:

```bash
git clone https://github.com/Xponer2/translator
git clone https://github.com/Xponer2/cucumber-back-to-life
git clone https://github.com/Xponer2/mystical-agriculture-back-to-life
cd mystical-agriculture-back-to-life
./gradlew build
```

Needs a JDK 21 to run Gradle. The jar is in `build/libs` (and those of the libraries in theirs).

## License

[MIT](LICENSE), © BlakeBr0 and the port's changes. This is a modified version of Mystical Agriculture by BlakeBr0,
changed to run on Minecraft 1.12.2.

NOT AN OFFICIAL MINECRAFT PRODUCT. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.
