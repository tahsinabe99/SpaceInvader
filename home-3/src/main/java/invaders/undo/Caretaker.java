package invaders.undo;

public class Caretaker {
    private Memento memento;

    public Memento getMemento(){
        return this.memento;
    }

    public void updateMemento(Memento memento){
        this.memento=memento;
    }


}
