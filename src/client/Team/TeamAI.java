package client.Team;

import Game.Cell;
import Game.Game;
import Game.Rat;

import java.util.ArrayList;
import java.util.List;

public class TeamAI extends client.AI {
    private ArrayList<Cell> poison = new ArrayList<>();
    private int score = 0;
    private int opsScore = 0;
    private ArrayList<Cell> blocks = new ArrayList<Cell>();

    @Override
    public String getTeamName() {
        return "Baha2r";
    }

    @Override
    public void think(Game game) {

        Cell[][] map = game.getMap();
        BreathFirstSearch bfs = new BreathFirstSearch(map);
        Rat myRat = game.getMyRat();
        Rat opsRat = game.getOppRat();
        ArrayList<Cell> getcheese = new ArrayList<>();
        for (int x = 0; x < game.getNumberOfRows(); x++) {
            for (int y = 0; y < game.getNumberOfColumns(); y++) {
                if (map[x][y].hasCheese()) {
                    getcheese.add(map[x][y]);
                }
            }
        }
        Eat2(opsRat, game);
        Cell cc = myRat.getCell();
        Cell dc = null;
        double i = 10000, j;
        for (Cell c : getcheese) {
            j = Manhatan(cc, c);
            if (j < i) {
                List<Cell> list = bfs.BFS(c, cc);
                if (list.get(0) == c && list.size() == 2) {
                    blocks.add(c);
                } else {
                    if (!poison.contains(c)) {
                        dc = c;
                        i = j;
                    }
                }
            }
        }
        if (i == 10000) {
            for (Cell c : blocks) {
                j = Manhatan(cc, c);
                if (j < i) {
                    if (!poison.contains(c)) {
                        dc = c;
                        i = j;
                    }
                }
            }
        }
        assert dc != null;
        if (Manhatan(cc, dc) == 0) {
            if (!poison.contains(myRat.getCell())) {
                Eat(myRat, game);
            }
        } else {
            ArrayList<Cell> way = bfs.BFS(cc, dc);
            Cell next_cell = way.get(way.size() - 1);
            int row = next_cell.getRow() - cc.getRow(), col = next_cell.getCol() - cc.getCol();
            if (row == 1)
                myRat.moveDown();
            if (row == -1)
                myRat.moveUp();
            if (col == 1)
                myRat.moveRight();
            if (col == -1)
                myRat.moveLeft();
            score = game.getMyScore();
            opsScore = game.getOppScore();
        }
    }

    private double Manhatan(Cell cell1, Cell cell2) {
        double a = cell1.getCol() - cell2.getCol();
        double b = cell1.getRow() - cell2.getRow();
        return Math.abs(a) + Math.abs(b);
    }

    private void Eat(Rat myRat, Game game) {
        myRat.eat();
        if (score > game.getMyScore()) {
            poison.add(myRat.getCell());
            think(game);

        }
    }

    private void Eat2(Rat opsRat, Game game) {
        if (opsScore != game.getOppScore()) {
            poison.add(opsRat.getCell());
        }
    }

}