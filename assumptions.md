## Assumptions

#### Character
* The character begins with health 20 and attack damage 3.
* Character can equip different types of weapons at the same time.
* Character can only equip one weapon of the same type at a time.
* Character bribing process is: check for sun stone -> check for sceptre -> check for treasure 
  -> check for one ring

#### Inventory
* Once the durability of weapon become 0 during the battle, it will be remove from the inventory
* Character will automatically replace weapon from inventory during battle if the one he is using 
  reached a durability of 0.

#### Static Entities
* Doors and keys are grouped by colour.
* Corresponding portals are indicating by the same colour.
* Portal is at layer 2, which represents as an obstecle.
* swamp tile behave as such: for swamp tile with movement_factor of 2, entity sit still for 1 tick

#### Moving Entities
* The spider begins with health 8 and attack damage 1.
* The spider spawns every 20 ticks.
* The zombie toast begins with health 10 and attack damage 2.
* The mercenary begins with health 16 and attack damage 3.
* The mercenary could be bribed with 1 treasure.
* The mercenary spawns every 60 ticks.
* The mercenary will not move if player is under invisible condtion.
* The mercenary will always follow the player if it become an ally (even when player is invisible).
* The battle radius of the mercenary is 5*5.
* The spider would not move, if that cell has a boulder.

#### Bosses
* The assassin begins with health 16 and attack damage 5.
* Assassin has 20% chance that will spawn in the place of a mercenary 
* The hydra begins with health 15 and attack damage 3.

#### Collectable Entities
* Doors and keys are grouped by colour.
* The invincibility potion would last for 10 ticks.
* The invisibility potion would last for 10 ticks.
* When the invisibility potion is used, no battle would take place.
* Once the bomb is placed, it could not be collected again.
* The bomb's blast radius is 3*3.
* The sword has a durability of 10 (number of times) and attack damage 2.
* The armour has a durability of 2. (number of times)
* The chance of zombies randomly spawning with armour is 20%.
* The chance of mercenaries having an armour is 33.3%.
* The chance of winning a "rare item" is 10%.
* Sun stone would be removed when it is used in building.

#### Buildable Entities
* 1 Treasure or 1 key is automatically used when a shield is built.
* The bow has a durability of 5 shots.
* The shield would decrease the effect of enemy attacks by 65%.
* The shield has a durability of 5. (number of times)
* Sceptre is used through interact and can only one enemy at a time
* Sceptre has a durability of 3

#### Game Modes
* In the hard mode, the player's health points is half of the standard mode.
* In the peaceful mode, the player can attack enemies.

#### Battle  
* If the character respawns, all old inventories would be able to carry to this new state.
* The character is attacked first, then enemies.
* If a type of weapon is out of durability, a new one would not be equipped within a battle.
* If a bow is equipped, the character attacks with all weapons twice.
* The health point is calculated to the nearest integer value by truncating its decimal points. 
* Armours equipped by enemies would not cost the durability when using.
* Mercenaries as allies can only attack the enemy and will not be attacked by the enemy.
* If a weapon is used up, it is automatically equipped if there is one in the inventory.
* During the battle, the health point of the hydra could exceed the initial health point.

#### Dungeon - Load File
* map file does not specify **BuildableEntities**
* will always receive a group identifier (e.g. **colour**, **key**) for **Door**, **Key**, **Portal**

#### Goal
* Goal for enemy can also achieved through bribing all the mercenary if the dungeon only contains mercenaries
* mercenary that become ally through sceptre will not be consider as achieving the enemy goal
* if no enemy exists at the beginning for dungeon goal for enemy is achieved
* if no treasure/switch exists at the beginning for dungeon goal for treasure/switch is achieved
* if no exit exists at the beginning for dungeon goal for exit will never achieved



