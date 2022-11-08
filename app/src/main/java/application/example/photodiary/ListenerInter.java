package application.example.photodiary;

//setting up listener interface for main activity's recycler view's adapter
public interface ListenerInter {
    public void delClicked(int id);
    public void editClicked(int id);
}
