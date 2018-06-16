import sys
import math

# thank you jmpeg for this print err function <3
def log(*args): print(*args, file=sys.stderr, flush=True)
#               UP     RIGHT  DOWN    LEFT
directions = [[0, -1], [1,0], [0,1], [-1,0]]

class Grid():
    def __init__(self):
        self.g = []
        self.width = 0
        self.height = 0
        
    def read(self):
        self.width = int(input())
        self.height = int(input())
        for i in range(self.height):
            self.g.append( input() )
            
    def __str__(self):
        return "\n".join(self.g)

grid = Grid()
grid.read()
log(grid)

# sanity_loss_lonely: how much sanity you lose every turn when alone, always 3 until wood 1
# sanity_loss_group: how much sanity you lose every turn when near another player, always 1 until wood 1
# wanderer_spawn_time: how many turns the wanderer take to spawn, always 3 until wood 1
# wanderer_life_time: how many turns the wanderer is on map after spawning, always 40 until wood 1
sanity_loss_lonely, sanity_loss_group, wanderer_spawn_time, wanderer_life_time = [int(i) for i in input().split()]
class Unit:
    def __init__(self,x ,y, id):
        self.x = x
        self.y = y
        self.id = id

    def distance(self, other):
        return abs(self.x - other.x) + abs(self.y - other.y)
        
    def __str__(self):
        return "{0} [{1}:{2}]".format(self.id,self.x, self.y)

class Explorer(Unit):
    def __init__(self, x, y, id, lights):
        Unit.__init__(self, x, y, id)
        self.lights = lights
        
    def __str__(self):
        return "Ex {0} [{1}:{2}] {3}".format(self.id,self.x, self.y, self.lights)
        
# game loop
while True:
    entity_count = int(input())  # the first given entity corresponds to your explorer
    explorers = []
    minions = []
    
    iHaveALight = False
    
    for i in range(entity_count):
        entity_type, id, x, y, param_0, param_1, param_2 = input().split()
        id = int(id)
        x = int(x)
        y = int(y)
        param_0 = int(param_0)
        param_1 = int(param_1)
        param_2 = int(param_2)
        if entity_type == "EXPLORER":
            explorer = Explorer(x, y, id, param_2)
            explorers.append( explorer )
        elif entity_type == "WANDERER":
            minion = Unit(x, y, id)
            minions.append( minion )
        elif entity_type == "EFFECT_LIGHT":
            iHaveALight = (id == 0) or iHaveALight
        
    # if no minion on the map, just move to the nearest player
    # if there is a minion next to you, move aside
    # if you have lights, you aren't using one, minions are not too close and explorer aren't too far
        -> light up
    # if a minion is close move away
    # otherwise go to the nearest player
