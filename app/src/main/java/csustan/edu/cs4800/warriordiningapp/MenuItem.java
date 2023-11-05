package csustan.edu.cs4800.warriordiningapp;

public class MenuItem {
    private String name;
    private String menuItemId;
    private String category;

    public MenuItem(String name, String menuItemId, String category) {
        this.name = name;
        this.menuItemId = menuItemId;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMenu() {
        return name + " " + menuItemId + " " + category;
    }

    public String getName() {
        return name;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getCategory() {
        return category;
    }

}

