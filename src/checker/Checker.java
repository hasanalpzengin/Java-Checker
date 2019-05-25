package checker;

public class Checker {
    int[] pos = {};
    int team;
    Type type;
    boolean isAlive;
    int id;

    public Checker(int[] pos, int team, Type type, int id) {
        this.pos = pos;
        this.team = team;
        this.type = type;
        this.isAlive = true;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
    
    public int getX(){
        return pos[0];
    }
    
    public int getY(){
        return pos[1];
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    public boolean isAlive(){
        return isAlive;
    }
    
    public void kill(){
        isAlive = false;
    }
    
}
