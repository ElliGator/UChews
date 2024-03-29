package com.example.elli.uchews;

/**
 * Created by Chris on 2/18/2016.
 */
public enum Cuisine {
    AMERICAN (348, "American", R.mipmap.ic_american, false),
    BBQ (349, "BBQ", R.mipmap.ic_bbq, false),
    BURGERS (351, "Burgers", R.mipmap.ic_bbq, false),
    CHINESE (352, "Chinese", R.mipmap.ic_chinese, false),
    FRENCH (356, "French", R.mipmap.ic_french, false),
    INDIAN (357, "Indian", R.mipmap.ic_chinese, false),
    ITALIAN (358, "Italian", R.mipmap.ic_italian, false),
    JAPANESE (359, "Japanese", R.mipmap.ic_japanese, false),
    KOREAN (360, "Korean", R.mipmap.ic_pizza, false),
    MEXICAN (361,"Mexican", R.mipmap.ic_mexican, false),
    MIDDLE_EASTERN (362, "Middle Eastern", R.mipmap.ic_middle_eastern, false),
    PIZZA (363, "Pizza", R.mipmap.ic_pizza, false),
    SEAFOOD (364, "Seafood", R.mipmap.ic_seafood, false),
    SUSHI (366, "Sushi", R.mipmap.ic_sushi, false),
    THAI (367, "Thai", R.mipmap.ic_thai, false),
    VEGETARIAN (368,"Vegetarian", R.mipmap.ic_bbq, false),


    // It may be better to make these ones "Dining Styles" as opposed to cuisines //
    /*BUFFETS (350, "Buffets"),
    DELIS (353, "Delis"),
    DINERS (354, "Diners"),
    FAST_FOOD (355, "Fast Food"),
    STEAKHOUSES (365, "Steakhouses"),*/
    //---------------------------------------------------------------------------//
    ;

    /**
     * The colloquial name for the cuisine (i.e. Asian).
     */
    private final String name;

    /**
     * The id used to filter factual queries.
     */
    private final int factual_id;

    /**
     * String of image resource associated with Cuisine
     */
    private final int image;

    private boolean inWheel;


    Cuisine(int factual_id, String name, int image, boolean inWheel) {
        this.name = name;
        this.factual_id = factual_id;
        this.image = image;
        this.inWheel = inWheel;
    }


    public String getName() {
        return name;
    }

    public int getFactual_id() {
        return factual_id;
    }

    public int getImage(){
        return image;
    }

    public boolean isInWheel(){
        return inWheel;
    }

    public void setInWheel(boolean val) {
        this.inWheel = val;
    }

    public static Cuisine getCuisineById(int cuisine_id){
        for(Cuisine c: Cuisine.values()){
            if(c.factual_id == cuisine_id)
                return c;
        }

        return null;
    }

    public static Cuisine getCuisine(String cuisine_string) {
        for(Cuisine c: Cuisine.values()) {
            if(c.getName().equals(cuisine_string)){
                return c;
            }
        }

        return null;
    }

}
