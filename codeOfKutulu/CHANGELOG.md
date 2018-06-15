# What's new

## 1.0

## 0.9.1
* #132 - Optimisation - dont add ground on cell with walls
* #137 - rework tiles to remove seams
* #123 - center map title text
* Add the ability to choose a map in parameters (map=name of map)
* #145 - add parametrized constants values based on seed
* #148 - increase base life of wanderers to 30

## 0.9
* #133 - add tentacles to map border fora fancy effect
* #119 - Limit Pacman map to wood leagues only
* #129 - A wanderer in range of a light effect will not have a yellow cell.
* #131 - The tooltip for wanderer reads "'till death", should be "'till recall".
* #128 - Explorers and minions share entity IDs. 
* #125 - rename sanity/life to param0 + put it after x,y
* #115 - fix INVALID MOVE bug
* #117 - tint ground in dark green if kutulu is coming

## 0.1.9
* #113 - all shelters refill every 50 turns, even if not depleted
* #114 - kutulu deals 50 sanity loss when only one player left

## 0.1.8 
* #62 - animation for the stunned players
* remove minions' circle indicating their range as it is now 1
* fix timeout bug & invalid actions
* make bosses deterministic

## 0.1.7
* Change fear sprite (again)
* #106 - Tweaked problem statement (and resolved #106)
* #107 - default AI for the bronze league
* #108 - isolation icon not always shown in first turn
* #104 - fix plan that still emited light effect
* more mazes

## 0.1.6
* #94 - last frame is not shown (bump to sdk 2.1)
* #100 - bump map healing to 3 (* number of players in range) and decreased shelter to 5
* #99 - do real BFS effects for PLAN & LIGHT
* wood3 & wood2 bosses - 1st draft
* #96 - add a move command with pathfinding
* #98 - Change leagues'rules


## 0.1.5
* #93 - fix stub / entityType was coded as in.nextLine()
* #92 - remove swapindex & send player with real IDs but each player its bot first
* #91 - change index to id
* #64 - add a EFFECT_YELL in inputs to announce who are stuck (by who) param1 & param2 are used for caster & receiver

## 0.1.4
* #85 - remove extra leagues
* #85 - rework default AI
* rework animation of minions (with color mask)
* remove some unused images
* #83 - red pentragram on spawn
* #82 - cleanup statement
* #81 - fix issue32 bias selection
* #80 - fix stub linesize
* #79 - no shelter input in wood

## 0.1.3
* fix bug when 4 players used light (alpha > 1.0 causes an exception in SDK)
* #issue 62 - inverse updateMinions & spreadMadness to avoid "Minions ignore dying players"
* #issue 24 - lower initial sanity of players (to 250) and start slashers at 200
* bump to SDK 1.36 (fix missing graphics glitch when texture are not in browser cache)
* map : walls are now sent with char '@' (old was '#') to please cg clojure bots :)

## 0.1.2
* slasher behavior : still go to old target if still in Line of sight
* rework the texture (may cause bugs in PIXI on 1st load)
* fix topleft flame graphic glitch
* rework slashers behaviour (see github's issue #44)
* handle problems in parameter SEED
* fix end screen
* add eulerscheZahl to test class (for replay)
* fixbug - map tool
* fixed minion size

## 0.1.1
* new logo
* statement has been reworked
* entityType are now strings ("EXPLORER", "WANDERER", "SLASHER", "EFFECT_SHELTER", "EFFECT_PLAN", "EFFECT_LIGHT")
* change some sprites (tombs, solo wall)
* stuck players will not be able to yell (on second stucked turn)
* slasher will now update their target when rushing (issue #44) 