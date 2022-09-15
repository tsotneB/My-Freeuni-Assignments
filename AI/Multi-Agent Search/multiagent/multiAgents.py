# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()
      #  print (legalMoves)
        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
      #  print(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"
      #  print(bestIndices)
      #  print(chosenIndex)
        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
        newGhostPos = successorGameState.getGhostPositions()
        minDistToFood = 9949345345990
        minDistToGhost = 9949345345990
        diff = 0
        for i in range(newFood.width):
            for j in range(newFood.height):
                if currentGameState.getFood()[i][j] and not newFood[i][j]:
                    diff = diff+1
                if newFood[i][j]:
                    minDistToFood = min(minDistToFood,util.manhattanDistance((i,j), (newPos)))
        if diff == 0:
            diff = 1/minDistToFood
        for pos in newGhostPos:
            minDistToGhost = min(minDistToGhost,util.manhattanDistance((i,j), (pos)))     
        scaredOverall = 0
        for timelapse in newScaredTimes:
            scaredOverall=scaredOverall+timelapse
        return successorGameState.getScore()+diff+minDistToGhost+scaredOverall

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def recursiveGetAction(self,gameState,currentAgent,currentDepth,agents):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        if currentAgent==0:
            currentDepth=currentDepth-1
            if currentDepth == 0:
                return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(currentAgent)
        currentList = list()
        for nextAction in actions:
            nextState = gameState.generateSuccessor(currentAgent,nextAction)
            currentList.append(self.recursiveGetAction(nextState,(currentAgent+1)%agents,currentDepth,agents))
        ans = currentList[0]
        if currentAgent==0:
            for actionScore in currentList:
                if actionScore>ans:
                    ans = actionScore
        else:
            for actionScore in currentList:
                if actionScore<ans:
                    ans = actionScore
        return ans
            
        
    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        recDepth = self.depth
        numAgents = gameState.getNumAgents()
        actions = gameState.getLegalActions(0)
        finalList = list()
        for nextAction in actions:
            nextState = gameState.generateSuccessor(0,nextAction)
            finalList.append((nextAction,self.recursiveGetAction(nextState,1,recDepth,numAgents)))
        ans = finalList[0]
        for actionScore in finalList:
             if actionScore[1]>ans[1]:
                 ans = actionScore
        return ans[0]
                                  
class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """
    
    def recursiveGetAction(self,gameState,currentAgent,currentDepth,agents,alpha,beta,printBool):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        if currentAgent==0:
            currentDepth=currentDepth-1
            if currentDepth == 0:
                return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(currentAgent)
        currentList = list()
        if currentAgent == 0:
            ans = -9949345345990
        else:
            ans = 9949345345990
        for nextAction in actions:
            nextState = gameState.generateSuccessor(currentAgent,nextAction)
            score = self.recursiveGetAction(nextState,(currentAgent+1)%agents,currentDepth,agents,alpha,beta,printBool)
            if currentAgent == 0:
                ans = max(ans,score)
                if beta is not None:
                    if ans>beta:
                        return ans
                if alpha is None:
                    alpha = ans        
                else:
                    alpha = max(alpha,ans)
            else:
                ans = min(ans,score)
                if alpha is not None:
                    if ans<alpha:
                        return ans
                if beta is None:
                    beta = ans
                else:
                    beta = min(beta,ans)
        if currentAgent == 0:
            if beta is None:
                beta = ans
            else:
                beta = min(beta,ans)
        else:
            if alpha is None:
                alpha = ans
            else:
                alpha = max(alpha,ans)
        return ans

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        recDepth = self.depth
        numAgents = gameState.getNumAgents()
        actions = gameState.getLegalActions(0)
        finalList = list()
        ans = -9949345345990
        bestMove = None
        alpha = None
        beta = None
        for nextAction in actions:
            if bestMove is None:
                bestMove = nextAction
            nextState = gameState.generateSuccessor(0,nextAction)
            printBool = False
            if recDepth == 2:
                printBool = True
            score = self.recursiveGetAction(nextState,1,recDepth,numAgents,alpha,beta,printBool)
            if score>ans:
                bestMove = nextAction
            ans = max(ans,score)
            if beta is not None:
                    if ans>beta:
                        return bestMove
            if alpha is None:
                alpha = ans        
            else:
                    alpha = max(alpha,ans)
            finalList.append((nextAction,score))
        return bestMove
class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """
    
    def recursiveGetAction(self,gameState,currentAgent,currentDepth,agents):
        if gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        if currentAgent==0:
            currentDepth=currentDepth-1
            if currentDepth == 0:
                return self.evaluationFunction(gameState)
        actions = gameState.getLegalActions(currentAgent)
        currentList = list()
        for nextAction in actions:
            nextState = gameState.generateSuccessor(currentAgent,nextAction)
            currentList.append(self.recursiveGetAction(nextState,(currentAgent+1)%agents,currentDepth,agents))
        ans = currentList[0]
        if currentAgent==0:
            for actionScore in currentList:
                if actionScore>ans:
                    ans = actionScore
        else:
            ans=sum(currentList)/len(currentList)
        return ans

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        recDepth = self.depth
        numAgents = gameState.getNumAgents()
        actions = gameState.getLegalActions(0)
        finalList = list()
        for nextAction in actions:
            nextState = gameState.generateSuccessor(0,nextAction)
            finalList.append((nextAction,self.recursiveGetAction(nextState,1,recDepth,numAgents)))
        ans = finalList[0]
        for actionScore in finalList:
             if actionScore[1]>ans[1]:
                 ans = actionScore
        return ans[0]

"Few Classes from my previous project that I used for betterEvaluationFunction"

class Actions:
    """
    A collection of static methods for manipulating move actions.
    """
    # Directions
    _directions = {Directions.NORTH: (0, 1),
                   Directions.SOUTH: (0, -1),
                   Directions.EAST:  (1, 0),
                   Directions.WEST:  (-1, 0),
                   Directions.STOP:  (0, 0)}

    _directionsAsList = _directions.items()

    TOLERANCE = .001

    def reverseDirection(action):
        if action == Directions.NORTH:
            return Directions.SOUTH
        if action == Directions.SOUTH:
            return Directions.NORTH
        if action == Directions.EAST:
            return Directions.WEST
        if action == Directions.WEST:
            return Directions.EAST
        return action
    reverseDirection = staticmethod(reverseDirection)

    def vectorToDirection(vector):
        dx, dy = vector
        if dy > 0:
            return Directions.NORTH
        if dy < 0:
            return Directions.SOUTH
        if dx < 0:
            return Directions.WEST
        if dx > 0:
            return Directions.EAST
        return Directions.STOP
    vectorToDirection = staticmethod(vectorToDirection)

    def directionToVector(direction, speed = 1.0):
        dx, dy =  Actions._directions[direction]
        return (dx * speed, dy * speed)
    directionToVector = staticmethod(directionToVector)

    def getPossibleActions(config, walls):
        possible = []
        x, y = config.pos
        x_int, y_int = int(x + 0.5), int(y + 0.5)

        # In between grid points, all agents must continue straight
        if (abs(x - x_int) + abs(y - y_int)  > Actions.TOLERANCE):
            return [config.getDirection()]

        for dir, vec in Actions._directionsAsList:
            dx, dy = vec
            next_y = y_int + dy
            next_x = x_int + dx
            if not walls[next_x][next_y]: possible.append(dir)

        return possible

    getPossibleActions = staticmethod(getPossibleActions)

    def getLegalNeighbors(position, walls):
        x,y = position
        x_int, y_int = int(x + 0.5), int(y + 0.5)
        neighbors = []
        for dir, vec in Actions._directionsAsList:
            dx, dy = vec
            next_x = x_int + dx
            if next_x < 0 or next_x == walls.width: continue
            next_y = y_int + dy
            if next_y < 0 or next_y == walls.height: continue
            if not walls[next_x][next_y]: neighbors.append((next_x, next_y))
        return neighbors
    getLegalNeighbors = staticmethod(getLegalNeighbors)

    def getSuccessor(position, action):
        dx, dy = Actions.directionToVector(action)
        x, y = position
        return (x + dx, y + dy)
    getSuccessor = staticmethod(getSuccessor)


class PositionSearchProblem():
    """
    A search problem defines the state space, start state, goal test, successor
    function and cost function.  This search problem can be used to find paths
    to a particular point on the pacman board.

    The state space consists of (x,y) positions in a pacman game.

    Note: this search problem is fully specified; you should NOT change it.
    """

    def __init__(self, gameState, costFn = lambda x: 1, goal=(1,1), start=None, warn=True, visualize=True):
        """
        Stores the start and goal.

        gameState: A GameState object (pacman.py)
        costFn: A function from a search state (tuple) to a non-negative number
        goal: A position in the gameState
        """
        self.walls = gameState.getWalls()
        self.startState = gameState.getPacmanPosition()
        if start != None: self.startState = start
        self.goal = goal
        self.costFn = costFn
        self.visualize = visualize
        if warn and (gameState.getNumFood() != 1 or not gameState.hasFood(*goal)):
            print('Warning: this does not look like a regular search maze')

        # For display purposes
        self._visited, self._visitedlist, self._expanded = {}, [], 0 # DO NOT CHANGE

    def getStartState(self):
        return self.startState

    def isGoalState(self, state):
        isGoal = state == self.goal

        # For display purposes only
        if isGoal and self.visualize:
            self._visitedlist.append(state)
            import __main__
            if '_display' in dir(__main__):
                if 'drawExpandedCells' in dir(__main__._display): #@UndefinedVariable
                    __main__._display.drawExpandedCells(self._visitedlist) #@UndefinedVariable

        return isGoal

    def getSuccessors(self, state):
        """
        Returns successor states, the actions they require, and a cost of 1.

         As noted in search.py:
             For a given state, this should return a list of triples,
         (successor, action, stepCost), where 'successor' is a
         successor to the current state, 'action' is the action
         required to get there, and 'stepCost' is the incremental
         cost of expanding to that successor
        """

        successors = []
        for action in [Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST]:
            x,y = state
            dx, dy = Actions.directionToVector(action)
            nextx, nexty = int(x + dx), int(y + dy)
            if not self.walls[nextx][nexty]:
                nextState = (nextx, nexty)
                cost = self.costFn(nextState)
                successors.append( ( nextState, action, cost) )

        # Bookkeeping for display purposes
        self._expanded += 1 # DO NOT CHANGE
        if state not in self._visited:
            self._visited[state] = True
            self._visitedlist.append(state)

        return successors

    def getCostOfActions(self, actions):
        """
        Returns the cost of a particular sequence of actions. If those actions
        include an illegal move, return 999999.
        """
        if actions == None: return 999999
        x,y= self.getStartState()
        cost = 0
        for action in actions:
            # Check figure out the next state and see whether its' legal
            dx, dy = Actions.directionToVector(action)
            x, y = int(x + dx), int(y + dy)
            if self.walls[x][y]: return 999999
            cost += self.costFn((x,y))
        return cost

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    startState = problem.getStartState()
    visited = []
    fringe = util.Queue()
    fringe.push(startState)
    visited.append(startState)
    parents = {}
    state = None
    while True:
        current = fringe.pop()
    #    print("Visited: ",visited)
        if problem.isGoalState(current):
            state = current
            break
        successors = problem.getSuccessors(current)
        for s in successors:
            node = s[0]
            action = s[1]
            if visited.count(node) == 0:
                lst=[current,action]
                parents[node]=lst
                fringe.push(node)
                visited.append(node)
    ans = []
    while not state == startState:
        p = parents[state]
        ans.append(p[1])
        state = p[0]
    ans.reverse()
    return ans

def mazeDistance(point1, point2, gameState):
    """
    Returns the maze distance between any two points, using the search functions
    you have already built. The gameState can be any game state -- Pacman's
    position in that state is ignored.

    Example usage: mazeDistance( (2,4), (5,6), gameState)

    This might be a useful helper function for your ApproximateSearchAgent.
    """
    x1, y1 = point1
    x2, y2 = point2
    walls = gameState.getWalls()
    assert not walls[x1][y1], 'point1 is a wall: ' + str(point1)
    assert not walls[x2][y2], 'point2 is a wall: ' + str(point2)
    prob = PositionSearchProblem(gameState, start=point1, goal=point2, warn=False, visualize=False)
    return len(breadthFirstSearch(prob))

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    pacmanPos = currentGameState.getPacmanPosition()
    ghostStates = currentGameState.getGhostStates()
    numGhosts = len(ghostStates)
    newScaredTimes = [ghostState.scaredTimer for ghostState in ghostStates]
    ghostPos = list()
    minDistToGhost = 9949345345990
    mazeDistances = list()
    for i in range(numGhosts):
        ghostPos.append((int(ghostStates[i].configuration.pos[0]),int(ghostStates[i].configuration.pos[1])))
        dist = mazeDistance(ghostPos[i], pacmanPos,currentGameState)
        if ghostStates[i].scaredTimer==0:
            minDistToGhost = min(minDistToGhost,dist)
        mazeDistances.append(dist)
   # ghostPos = currentGameState.getGhostPositions()
    foodGrid = currentGameState.getFood()
    bonus = 0
    for i in range(numGhosts):
        distanceToGhost = mazeDistances[i]
        if distanceToGhost < newScaredTimes[i]:
            bonus += 1
    minDistToFood = 9949345345990
    PQueue = util.PriorityQueue()
    for i in range(foodGrid.width):
        for j in range(foodGrid.height):
            if foodGrid[i][j]:
                PQueue.push((i,j), util.manhattanDistance((i,j), pacmanPos))
    for i in range(4):
        if PQueue.isEmpty():
            break
        minDistToFood=min(minDistToFood,mazeDistance(pacmanPos,PQueue.pop(),currentGameState))
    if foodGrid.count() == 0:
        minDistToFood = 1
   # return evaluationValue
    return currentGameState.getScore()-minDistToFood - foodGrid.count() + 100*bonus - 1/max(1,minDistToGhost)
# Abbreviation
better = betterEvaluationFunction