package ir.boojanco.onlinefoodorder.ui.fragments.home;

public class FoodTypeFilterItem {
    private String name;
    private int pic;

    public String getName() {
        return name;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodTypeFilterItem(String name, int pic) {
        this.name = name;
        this.pic = pic;
    }
}
