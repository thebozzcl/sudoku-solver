# sudoku-solver
Sudoku solver based on exact cover problem solvers.

## v1

First implementation, using Knuth's Algorithm X plus a few optimizations I came up with:
* NoRepeatStack: I don't like recursion, so I flattened it by using a processing stack. To avoid processing duplicate
                 data, I tracked the hash of each processed state and skipped ones we already queued up.

I implemented three different exact cover problems:
* Sudoku: it can at the very least find individual solutions, but it takes forever to find all solutions. I don't know
          if it's just a performance issue.
* Wiki example: the simple example taken from the Algorithm X wikipedia article.
* LinkedIn Queens: not quite working, even though I'm sure the constraints are right. It's probably an algorithm
                   performance issue

## v2

TBD, but I want to:
* Improve the library model to separate the problem definition from the solution steps
* Add a way to filter out choices before iterating, as an additional way to optimize solutions
* Switch to the dancing links algorithm, though I still don't want to use recursion, so I need to figure out what the
  stack definition is going to look like