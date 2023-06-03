# COMP2521 T13B_ELDERBERRY Group Meeting Minutes

## Meeting 1

### 1. Date:
From 3pm to 4.40pm, Mon 18th Oct 2021

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items
* Milestone 1: Go through the specification together
  - Character
  - Entity (Static, Moving, Collectable, Buidable, Interactable)
  - GameMode (Peaceful, Standard, Hard) - Strategy Pattern
  - Goal

### 4. Action Items
* Planning for Milestone 1 (timeline included)
* Schedule next meeting: 4pm Tue 19th Oct 2021  
    TODO:
    - Design & UML Diagram
    - Assumptions
    - Delegating tasks
    - Timeline for Milestone 2 & 3

## Meeting 2

### 1. Date:
From 6pm to 9pm, Tue 19th Oct 2021

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items
* Milestone 1: Discuss the design
  - Classes: Character, Dungeon, Game, Entity, Goals
  - Interface: GameMode(Strategy Pattern)
  - Observer Pattern: goal - dungeon
  - Assumption: save game for same name

### 4. Action Items
* Draw UML diagram together
* Schedule next meeting: 12pm Wed 20th Oct 2021  

## Meeting 3

### 1. Date:
From 12pm to 3.40pm, Wed 20th Oct 2021

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items
* Milestone 1: Discuss the design
  - Composite pattern: GoalComponent

### 4. Action Items
* Draw UML diagram together
  - Added methods
* Schedule next meeting: 3pm Tue 26th Oct 2021  

## Meeting 4

### 1. Date:
From 3pm to 6pm, Tue 26th Oct 2021

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 2
* Discuss the added assumptions
* Discuss the implementation of using collectable entities
* Discuss the implementation of chance of winning a "rare item" and chance of zombies/mercenaries with armour

### 4. Action Items
* Added assumptions
* Schedule next meeting: 4pm Fri 29th Oct 2021  

## Meeting 5

### 1. Date:
From 4pm to 7pm, Fri 29th Oct 2021 

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 2
* Discuss the action order within a tick
* Discuss the implementation of enemies running away from the character when they are invincible
* Discuss the matching of door and key

### 4. Action Items
* Implement the functionality of using potion
* Implement the tick in dungeon
* Schedule next meeting: 4pm Sun 31th Oct 2021  

  TODO **BEFORE** next meeting:
  - finish unit & system testing for our own parts
  - javadoc  

  TODO on next meeting:
  - merge to master
  - overall refine

## Meeting 6

### 1. Date:
4pm Sun 31th Oct 2021 

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 2
* Assumptions added

### 4. Action Items
* Merge to master

## Meeting 7

### 1. Date:
From 4pm to 7pm, Fri 5th Nov 2021 

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 3
* Boss interface (Assasin extends Mercenary & Hydra)
* Assasin 
  1. battle -> more damage
  2. less than 30% chance to spawn
  3. bribe = the one ring + treasure
* Hydra
  1. spawn in HardMode every 50 ticks
  2. move (zombie toast)
  3. battle -> 50% chance
* Swamp tile extends StaticEntity
  1. MovingEntity (field int frozen)
* Sun Stone
  1. build - remove
  2. bribe
  3. open doors
  4. add field in character (SunStone sunStone)
* Anduril
  1. battle - triple damage against bosses
  2. battle - hydra 0% to raise health
* Sceptre
  1. build
  2. use - remove from inventory, last for 10 ticks -> mercenary allies vs. temp allies
* Midnight Armour implements EntityEquip
  1. build - if no zombies (dungeon - hasZombie())
  2. battle - extra attack damage and protection
- - - 
* Dungeon Builder
  1. Dungeon(int xStart, int yStart, int xEnd, int yEnd)
* Logic Switch
  1. Door - LogicGate
  2. wire connected to wire?

Interface (stored as List<> in Character)
* weapon: sword, bow, anduril, midnight armour
* defense: armour, shield, midnight armour

### 4. Action Items
* fix some problems in Milestone 2
* Work distribution
* planning

## Meeting 8

### 1. Date:
From 4pm to 5.45pm, Fri 12th Nov 2021 

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 3
* To-dos: refine save/load game, generate dungeon, refactor battle, refactor dungeon(if possible), overall testing
* decisions on rerunning m2 autotests
* updates on coverage issue & autotests patches

### 4. Action Items
* merge functionalities already done into master

## Meeting 9

### 1. Date:
Sun 14th Nov 2021 

### 2. Attendees
* Claire Jiang
* Elisabeth Zhu
* Weiqi Wang
* Xiuwen Shi
* Yanjie Zhou

### 3. Discussion Items - Milestone 3
* fixed failed tests

### 4. Action Items
* merge into master
* refine style