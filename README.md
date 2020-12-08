# Smash

Smash is a Minecraft gamemode inspired by Nintendo's Super Smash Bros. The aim of the game is to knock your opponents off the map to gain points, using your special kit items, as well as items (power-ups) picked up around the map. The more damage you recieve, the more knockback you recieve on each hit. _Disclaimer: The design of this plugin is modelled from a retired Minecraft server called McPVP. Subsequently, some items and abilities are derived/inspired by them._

**YouTube Showcase:** https://youtu.be/B4x7BLId1F4


## Kits
This is a list of currently completed kits, including their items and abilities. Every kit comes with a double-jump rocket, allowing you to jump mid air!

**Blink**
- 3 Double-Jumps
- Iron Sword
- Bow (Infinity) and Arrow
- Switcher Ball (_snowball_) (switches your location with an opponent's upon a successful throw)

**Metoo**
- 2 Double-Jumps
- Iron Sword
- Psychic Orb (_snowball_) (makes your opponent dizzy upon a successful throw)
- Teleporter (_ender pearl_) (teleports you a short distance ~0.5 seconds after thrown)

**Pika**
- 2 Double Jumps
- Iron Sword
- Lunge (_golden carrot_) (thrusts you forward)
- Thunder Jolt (_wooden axe_) (calls lightning from the sky to strike your opponents)

**Toshi**
- 2 Double Jumps
- Iron Sword
- Ground Pound (_clay brick_) (slams you into the ground if you're in the sky, causing damage and knockback to those around you)
- Toshi Egg (_creeper spawn egg_) (throws an egg at your opponent, dealing damage and knockback)

**Jigglyo**
- 1 Double Jump
- Iron Sword
- Healing Ability (crouch on the ground to reduce your knockback level)
- 5 Mini Double Jumps (used after your initial double jump)

**Shadow**
- 2 Double Jumps
- Thunder Jolt (_wooden axe_) (calls lightning from the sky to strike your opponents)
- Bow (Infinity) and Arrow
- Switcher Ball (_snowball_) (switches your location with an opponent's upon a successful throw)
- Lunge (_golden carrot_) (thrusts you forward)


## Power-ups
Power-ups are special items that spawn randomly in intervals around the map. Power-ups provide an extra edge over your opponents, if you're able to claim them first!
Be careful though, as power-ups have limited uses and you will lose them if you get knocked into the void before utilizing them.

- **Swift Feather** (provides a speed boost and jump boost for 10 seconds)
- **Maxim Apple** (reduces your knockback level by 50)
- **Golden Maxim Apple** (resets your knockback level to 1)
- **Home-Run Bat** (_stick_) (deals additional knockback to your opponent)
- **Hammer** (_iron axe_) (deals significant knockback to your opponent. Warning: because of this item's power, it will be the only item in your inventory for a short duration upon picking it up)
- **TNT** (_throwable primed TNT_) (will explode 2 seconds after throwing)
- **Land Mine** (_stone pressure plate_) throwable land mine that will stick to the ground when it lands. When a player walks on it, an explosion will be created)

## Commands
Commands | Parameters | Description
-------- | ----------- | ------------
/kit | ```[kitName]``` | Gives the player a kit. Displays kit list if no kit selected
/kits | ```[kitName]``` | Gives the player a kit. Displays kit list if no kit selected
/speed | ```<1-10>``` | Changes your fly speed
/world | | Displays the world name
/start | ```[mapName]``` | Starts the Smash game
/tpw | ```<worldName>``` | Teleports to a specific world. Generates a new world if name doesn't exist
/end | | Forcibly ends the Smash game
/maps | | Displays a list of all maps currently in rotation
/setspawn | | Sets the current world's spawn at the player's current location




