# sudoku-solver
Sudoku solver based on exact cover problem solvers.

## v1

First implementation, using Knuth's Algorithm X plus a few optimizations I came up with:
* NoRepeatStack: I don't like recursion, so I flattened it by using a processing stack. To avoid processing duplicate
                 data, I tracked the hash of each processed state and skipped ones we already queued up.

## v2

TBD, but I want to:
* Improve the library model to separate the problem definition from the solution steps
* Add a way to filter out choices before iterating, as an additional way to optimize solutions
* Switch to the dancing links algorithm, though I still don't want to use recursion, so I need to figure out what the
  stack definition is going to look like