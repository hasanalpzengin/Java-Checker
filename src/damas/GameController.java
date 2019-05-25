/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import checker.Checker;
import checker.Type;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author hasalp
 */
public class GameController {

    private static ArrayList<Checker> checkers = new ArrayList<>();
    private static ArrayList<Checker> mandatoryJump = new ArrayList();
    //board design
    // 0-> no team
    // 1-> team 1
    // 2-> team 2
    private static final int[][] START_POSITIONS = {
        {0, 1, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 0},
        {0, 1, 0, 1, 0, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {2, 0, 2, 0, 2, 0, 2, 0},
        {0, 2, 0, 2, 0, 2, 0, 2},
        {2, 0, 2, 0, 2, 0, 2, 0}
    };

    private static int team_1_checkers = 12;
    private static int team_2_checkers = 12;
    private static boolean isGameProgress = true;

    public static ArrayList<Checker> getCheckers() {
        return checkers;
    }

    public static Checker getChecker(int x, int y) {
        int i = 0;
        for (Checker checkerCursor : checkers) {
            if (checkerCursor.getX() == x && checkerCursor.getY() == y && checkerCursor.isAlive()) {
                return checkerCursor;
            }
            i++;
        }
        return null;
    }

    private static void setChecker(Checker checker, int x, int y) {
        checker.setPos(new int[]{x, y});
        checkers.set(checker.getId(), checker);
    }

    private static void killChecker(Checker checker) {
        System.out.println("Killed");
        checker.kill();
        checkers.set(checker.getId(), checker);
        if (checker.getTeam() == 1) {
            team_1_checkers--;
        } else {
            team_2_checkers--;
        }
    }

    public static void initGame() {
        checkers.clear();
        mandatoryJump.clear();
        int id = 0;
        for (int i = 0; i < map.Board.BOARD_DIMENSIONS[0]; i++) {
            for (int j = 0; j < map.Board.BOARD_DIMENSIONS[1]; j++) {
                if (START_POSITIONS[i][j] == 1 || START_POSITIONS[i][j] == 2) {
                    checkers.add(new Checker(new int[]{j, i}, START_POSITIONS[i][j], new Type("Basic", 1), id));
                    id++;
                }
            }
        }

        team_1_checkers = 12;
        team_2_checkers = 12;
        isGameProgress = true;
    }

    public static boolean move(Checker selectedChecker, int x, int y) {
        if (isGameProgress) {
            //if mandatory jump exist
            if (mandatoryJump.size() > 0) {
                boolean isMovementMandatory = false;
                for (Checker checker : mandatoryJump) {
                    if (checker.getId() == selectedChecker.getId()) {
                        isMovementMandatory = true;
                    }
                }
                if (!isMovementMandatory) {
                    return false;
                }
            }
            //move search
            //if king
            if (selectedChecker.getType().getPower() == 2) {
                if (movementCheckKing(selectedChecker, x, y)) {
                    setChecker(selectedChecker, x, y);
                    int opponent = selectedChecker.getTeam() == 1 ? 2 : 1;
                    mandatoryJump = mandatoryJumpScan(opponent);
                    return true;
                } else if (attackKing(selectedChecker, x, y)) {
                    setChecker(selectedChecker, x, y);
                    //win check
                    if (winnerCheck() != 0) {
                        JOptionPane.showMessageDialog(null, selectedChecker.getTeam() + " Team Wins");
                    }
                    //extra jump search after attack
                    if (isExtraJump(selectedChecker)) {
                        mandatoryJump.add(selectedChecker);
                        System.out.println("Mandatory Jump Found");
                    } else {
                        mandatoryJump.clear();
                        int opponent = selectedChecker.getTeam() == 1 ? 2 : 1;
                        mandatoryJump = mandatoryJumpScan(opponent);
                        System.out.println("Mandatory Jump not Found");
                    }
                    return true;
                }
            } else {
                if (movementCheck(selectedChecker, x, y)) {
                    kingCheck(selectedChecker, x, y);
                    setChecker(selectedChecker, x, y);
                    int opponent = selectedChecker.getTeam() == 1 ? 2 : 1;
                    mandatoryJump = mandatoryJumpScan(opponent);
                    return true;
                } else if (attack(selectedChecker, x, y)) {
                    kingCheck(selectedChecker, x, y);
                    setChecker(selectedChecker, x, y);
                    if (winnerCheck() != 0) {
                        JOptionPane.showMessageDialog(null, selectedChecker.getTeam() + " Team Wins");
                    }
                    if (isExtraJump(selectedChecker)) {
                        mandatoryJump.add(selectedChecker);
                        System.out.println("Mandatory Jump Found");
                        return false;
                    } else {
                        mandatoryJump.clear();
                        int opponent = selectedChecker.getTeam() == 1 ? 2 : 1;
                        mandatoryJump = mandatoryJumpScan(opponent);
                        System.out.println("Mandatory Jump not Found");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean movementCheck(Checker selectedChecker, int x, int y) {
        //forward movement check
        if (selectedChecker.getTeam() == 1) {
            //cross movement allowed
            if ((selectedChecker.getY() == y - 1 && selectedChecker.getX() == x + 1) || (selectedChecker.getY() == y - 1 && selectedChecker.getX() == x - 1)) {
                return true;
            }
        } else {
            if ((selectedChecker.getY() == y + 1 && selectedChecker.getX() == x - 1) || (selectedChecker.getY() == y + 1 && selectedChecker.getX() == x + 1)) {
                return true;
            }
        }
        return false;
    }

    private static boolean kingCheck(Checker selectedChecker, int x, int y) {
        // king check
        if ((selectedChecker.getTeam() == 1 && y == map.Board.BOARD_DIMENSIONS[1] - 1) || (selectedChecker.getTeam() == 2 && y == 0)) {
            for (Checker checker : checkers) {
                if (checker.isAlive()) {
                    if (checker.getId() == selectedChecker.getId()) {
                        selectedChecker.setType(new Type("King", 2));
                        checkers.set(selectedChecker.getId(), selectedChecker);
                        System.out.println("King Set");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean attack(Checker selectedChecker, int x, int y) {
        if (selectedChecker.getTeam() == 1) {
            //down-right
            if (selectedChecker.getY() == y - 2 && selectedChecker.getX() == x - 2) {
                Checker killedChecker = getChecker(x - 1, y - 1);
                if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                    killChecker(killedChecker);
                    return true;
                }
                //down left
            } else if (selectedChecker.getY() == y - 2 && selectedChecker.getX() == x + 2) {
                Checker killedChecker = getChecker(x + 1, y - 1);
                if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                    killChecker(killedChecker);
                    return true;
                }
            }
        } else {
            //up-left
            if (selectedChecker.getY() == y + 2 && selectedChecker.getX() == x + 2) {
                Checker killedChecker = getChecker(x + 1, y + 1);
                if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                    killChecker(killedChecker);
                    return true;
                }
                //up-right
            } else if (selectedChecker.getY() == y + 2 && selectedChecker.getX() == x - 2) {
                Checker killedChecker = getChecker(x - 1, y + 1);
                if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                    killChecker(killedChecker);
                    return true;
                }
            }
        }
        return false;
    }

    private static int winnerCheck() {
        if (team_1_checkers == 0) {
            //team 2 wins
            isGameProgress = false;
            return 2;
        } else if (team_2_checkers == 0) {
            //team 1 wins
            isGameProgress = true;
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean movementCheckKing(Checker selectedChecker, int x, int y) {
        return (selectedChecker.getY() == y - 1 && selectedChecker.getX() == x + 1)
                || (selectedChecker.getY() == y - 1 && selectedChecker.getX() == x - 1)
                || (selectedChecker.getY() == y + 1 && selectedChecker.getX() == x - 1)
                || (selectedChecker.getY() == y + 1 && selectedChecker.getX() == x + 1);
    }

    private static boolean attackKing(Checker selectedChecker, int x, int y) {
        //down-right
        if (selectedChecker.getY() == y - 2 && selectedChecker.getX() == x - 2) {
            Checker killedChecker = getChecker(x - 1, y - 1);
            if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                killChecker(killedChecker);
                return true;
            }
            //down-left
        } else if (selectedChecker.getY() == y - 2 && selectedChecker.getX() == x + 2) {
            Checker killedChecker = getChecker(x + 1, y - 1);
            if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                killChecker(killedChecker);
                return true;
            }
            //up-left
        } else if (selectedChecker.getY() == y + 2 && selectedChecker.getX() == x + 2) {
            Checker killedChecker = getChecker(x + 1, y + 1);
            if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                killChecker(killedChecker);
                return true;
            }
            //up-right
        } else if (selectedChecker.getY() == y + 2 && selectedChecker.getX() == x - 2) {
            Checker killedChecker = getChecker(x - 1, y + 1);
            if (killedChecker != null && killedChecker.getTeam() != selectedChecker.getTeam()) {
                killChecker(killedChecker);
                return true;
            }
        }
        return false;
    }

    /* todo */
    public static ArrayList<Checker> mandatoryJumpScan(int team) {
        ArrayList<Checker> jumps = new ArrayList();
        System.out.println("Mandatory Jump Scan for: " + team);
        for (Checker checker : checkers) {
            //for each ally team
            if (checker.getTeam() == team) {
                if (checker.isAlive()) {
                    if (isExtraJump(checker)) {
                        jumps.add(checker);
                        System.out.println("Jump Found..");
                    }
                }
            }
        }
        return jumps;
    }

    public static boolean isExtraJump(Checker checker) {
        //if not king
        if (checker.getType().getPower() < 2) {
            if (checker.getTeam() == 1) {
                //right bottom
                if (getChecker(checker.getX() + 1, checker.getY() + 1) != null
                        && getChecker(checker.getX() + 1, checker.getY() + 1).getTeam() != checker.getTeam()
                        && getChecker(checker.getX() + 2, checker.getY() + 2) == null
                        && checker.getX() + 2 < map.Board.BOARD_DIMENSIONS[0]
                        && checker.getY() + 2 < map.Board.BOARD_DIMENSIONS[1]) {
                    return true;
                    //left bottom
                } else if (getChecker(checker.getX() - 1, checker.getY() + 1) != null
                        && getChecker(checker.getX() - 1, checker.getY() + 1).getTeam() != checker.getTeam()
                        && getChecker(checker.getX() - 2, checker.getY() + 2) == null
                        && checker.getX() - 2 >= 0
                        && checker.getY() + 2 < map.Board.BOARD_DIMENSIONS[1]) {
                    return true;
                }
            } else {
                //right top
                if (getChecker(checker.getX() + 1, checker.getY() - 1) != null
                        && getChecker(checker.getX() + 1, checker.getY() - 1).getTeam() != checker.getTeam()
                        && getChecker(checker.getX() + 2, checker.getY() - 2) == null
                        && checker.getX() + 2 < map.Board.BOARD_DIMENSIONS[0]
                        && checker.getY() - 2 >= 0) {
                    return true;
                    //left top
                } else if (getChecker(checker.getX() - 1, checker.getY() - 1) != null
                        && getChecker(checker.getX() - 1, checker.getY() - 1).getTeam() != checker.getTeam()
                        && getChecker(checker.getX() - 2, checker.getY() - 2) == null
                        && checker.getX() - 2 >= 0
                        && checker.getY() - 2 >= 0) {
                    return true;
                }
            }
        } else {
            //right top
            if (getChecker(checker.getX() + 1, checker.getY() - 1) != null && getChecker(checker.getX() + 1, checker.getY() - 1).getTeam() != checker.getTeam() && getChecker(checker.getX() + 2, checker.getY() - 2) == null && checker.getX() + 2 < map.Board.BOARD_DIMENSIONS[0] && checker.getY() - 2 >= 0) {
                return true;
                //left top
            } else if (getChecker(checker.getX() - 1, checker.getY() - 1) != null && getChecker(checker.getX() - 1, checker.getY() - 1).getTeam() != checker.getTeam() && getChecker(checker.getX() - 2, checker.getY() - 2) == null && checker.getX() - 2 >= 0 && checker.getY() - 2 >= 0) {
                return true;
                //right bottom
            } else if (getChecker(checker.getX() + 1, checker.getY() + 1) != null && getChecker(checker.getX() + 1, checker.getY() + 1).getTeam() != checker.getTeam() && getChecker(checker.getX() + 2, checker.getY() + 2) == null && checker.getX() + 2 < map.Board.BOARD_DIMENSIONS[0] && checker.getY() + 2 < map.Board.BOARD_DIMENSIONS[1]) {
                return true;
                //left bottom
            } else if (getChecker(checker.getX() - 1, checker.getY() + 1) != null && getChecker(checker.getX() - 1, checker.getY() + 1).getTeam() != checker.getTeam() && getChecker(checker.getX() - 2, checker.getY() + 2) == null && checker.getX() - 2 >= 0 && checker.getY() + 2 < map.Board.BOARD_DIMENSIONS[1]) {
                return true;
            }
        }
        return false;
    }

}
