package csustan.edu.cs4800.warriordiningapp;

public class MenuItem {
    private static String name;
    private static String menuItemId;
    private static String category;

    public MenuItem() {

    }

    public MenuItem(String name, String menuItemId, String category) {
        this.name = name;
        this.menuItemId = menuItemId;
        this.category = category;
    }

    public static int menuLength(MenuItem[] menu) {
        int menuSize = menu.length;

        return menuSize;
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

    public static String getName(MenuItem menu) {
        return name;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public static String getCategory(MenuItem breakfastMenu) {
        return category;
    }

}

