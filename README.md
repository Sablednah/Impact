Impact
==========

Falling players make impact craters!  Now with up to 100% more flying blocks!


### Configuration

    debugMode: false
Enable extra debugging messages in server logs.

    heavyMobs: [Giant]
List of entities not thrown into air by an Impact

	impactMobs: [All]
List of mob types that cause Impacts

    playersImpact: true
Do players Impact?  This overrides the permissions node.  Set to true and use the node if you require per/player control - for example as a passive/permissions skill for Heroes.

    playersHeavy: false
Are players heavy?

    minFall: 5
distance fallen before impacts "crater".  Settings less than 5 may cause impacts as part of regular Mob pathfinding - be warned.
	
    impactTpye: explode
Determines the type of impact.
Explode produces an explosion at the point of impact.  The impacting mob is not damaged by this explosion, others are as normal.
Rise throws the blocks around directly upwards into the air.
Scatter throws blocks up and slightly away from the impact point.

These configure options only apply to explosions.

    explosionScale: 10.0
For every explosionScale blocks dropped (over the minimum) an extra 1 is added to explosion strength.  
E.g. by default config - a fall of 36 blocks would yeild ((36-5)/10)+1 = 4  the same as TNT - http://minecraft.gamepedia.com/Explosion


These configuration options only apply to rise and scatter.
    fixHoles: false
Only applied if CreeperHeal is present.  If so, flung blocks are passed to CreeperHeal.  It's recommended that you edit CreeperHeal's advanced.yml to set drop-overwritten-blocks: to false OR set blockFalling to true to prevent duplicates.

    dropBlocks: false
Should dropped blocks drop items if they break on landing?

    blockFalling: false
Should falling blocks be stopped from landing?  If true blocks that rise/scatter will vanish on landing - best used with CreeperHeal and fixHoles set to true

    dropChance: 95
%age of blocks that don't drop on breaking.
	
	throwScale: 5.0
For every throwScale blocks dropped (over the minimum) an extra 1 is added to impact radius.  
E.g. by default config - a fall of 36 blocks would give radius ((36-5)/5)+1 = 7 blocks
	

These configuration options only apply to scatter.
	spread: 0.1
How far outward blocks are scattered.


	
### Commands

	/impact reload
Reloads current configuration.


### Permissions


	impact.nocrater
Players with this node do not leave craters.
	

### Changelog

1.0:  First release.


To Do
=====
Add an acceptable level of block protection!


Known Bugs/Conflicts
====================

?
